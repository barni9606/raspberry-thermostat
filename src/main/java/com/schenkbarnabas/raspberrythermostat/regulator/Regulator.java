package com.schenkbarnabas.raspberrythermostat.regulator;

import com.pi4j.io.gpio.*;
import com.schenkbarnabas.raspberrythermostat.model.Period;
import com.schenkbarnabas.raspberrythermostat.services.TemperatureService;
import com.schenkbarnabas.raspberrythermostat.services.WeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class Regulator {

    private TemperatureService temperatureService;

    private GpioController gpio;

    private GpioPinDigitalOutput relay;

    private Predictor predictor;

    @Autowired
    public Regulator(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
        predictor = new LinearPredictor();
        gpio = GpioFactory.getInstance();
        relay = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "relay", PinState.HIGH);
        gpio.setShutdownOptions(true, PinState.LOW);
    }

    @Scheduled(fixedRate = 30000)
    public void regulate() {
        int day = LocalDate.now().getDayOfWeek().ordinal();
        int now = LocalDateTime.now().getHour() * 60 + LocalDateTime.now().getMinute();

        float temp = currentPeriod(day, now).getTemp();
        Period nextPeriod = nextPeriod(day, now);

        int remainingTimeUntilNextPeriod = nextPeriod.getStart().getTimeInt() - now;

        if (nextPeriod.getStart().getTimeInt() < now) {
            remainingTimeUntilNextPeriod += 24 * 60;
        }

        if (nextPeriod.getTemp() > temp
                && remainingTimeUntilNextPeriod < predictor.predict(temperatureService.getCurrentTemp(), nextPeriod.getTemp())){
            predictor.startPredictionTime(temperatureService.getCurrentTemp());
            raise();
        } else {
            stagnate(temp);
        }
    }

    private void stagnate(float temp) {
        predictor.error(temperatureService.getCurrentTemp(), temp);
        if (temp > temperatureService.getCurrentTemp() + 0.5f) {
            activateRelay();
        } else if (temp < temperatureService.getCurrentTemp() - 0.5f) {
            deactivateRelay();
        }
    }

    private void raise() {
        activateRelay();
    }


    private Period currentPeriod(int day, int now) {
        return WeekService.getCurrentWeek().get(day).getPeriods().stream().filter(
                period -> period.getStart().getTimeInt() <= now && now < period.getFinish().getTimeInt()
        ).findFirst().get();
    }

    private Period nextPeriod(int day, int now) {
        Period current = currentPeriod(day, now);
        if (current.getFinish().isEndOfDay()) {
            return WeekService.getCurrentWeek().get((day + 1) % 6).getPeriods().get(0);
        }
        int currentIndex = WeekService.getCurrentWeek().get(day).getPeriods().indexOf(current);
        return WeekService.getCurrentWeek().get(day).getPeriods().get(currentIndex + 1);

    }

    private void activateRelay() {
        relay.low();
        log.info("relay activated");
    }

    private void deactivateRelay() {
        relay.high();
        log.info("relay deactivated");
    }

    @PreDestroy
    public void dispose() {
        log.info("Shutting down GPIO controllers, and tasks.");
        relay.high();
        gpio.shutdown();
    }
}
