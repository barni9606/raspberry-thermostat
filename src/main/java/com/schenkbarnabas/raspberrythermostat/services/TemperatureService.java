package com.schenkbarnabas.raspberrythermostat.services;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import com.schenkbarnabas.raspberrythermostat.exceptions.TemperatureSensorNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

    private TemperatureSensor sensor;

    public TemperatureService() {
        W1Master w1Master = new W1Master();
        sensor = w1Master.getDevices(TemperatureSensor.class).stream()
                .findFirst().orElseThrow(TemperatureSensorNotFoundException::new);
    }

    /**
     * Only for unit testing.
     * @param sensor mocked sensor
     */
    public TemperatureService(TemperatureSensor sensor) {
        this.sensor = sensor;
    }

    public float getCurrentTemp() {
        return (float) sensor.getTemperature(TemperatureScale.CELSIUS);
    }
}
