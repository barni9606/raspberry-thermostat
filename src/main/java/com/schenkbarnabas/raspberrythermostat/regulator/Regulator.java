package com.schenkbarnabas.raspberrythermostat.regulator;

import com.schenkbarnabas.raspberrythermostat.model.Period;
import com.schenkbarnabas.raspberrythermostat.services.TemperatureService;
import com.schenkbarnabas.raspberrythermostat.services.WeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class Regulator {

    @Autowired
    private TemperatureService temperatureService;

    private Predictor predictor = new LinearPredictor();

    @Scheduled(fixedRate = 30000)
    public void regulate() {
        int day = LocalDate.now().getDayOfWeek().ordinal();
        int now = LocalDateTime.now().getHour() * 60 + LocalDateTime.now().getMinute();

        float temp = currentPeriod(day, now).getTemperature();
        Period nextPeriod = nextPeriod(day, now);

        int remainingTimeUntilNextPeriod = nextPeriod.getStart().getTimeInt() - now;

        if (nextPeriod.getStart().getTimeInt() < now) {
            remainingTimeUntilNextPeriod += 24 * 60;
        }

        if (nextPeriod.getTemperature() > temp
                && remainingTimeUntilNextPeriod < predictor.predict(temperatureService.getCurrentTemp(), nextPeriod.getTemperature())){
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
        log.info("relay activated");
    }

    private void deactivateRelay() {
        log.info("relay deactivated");
    }
}
