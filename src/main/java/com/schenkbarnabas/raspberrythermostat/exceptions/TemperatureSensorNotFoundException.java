package com.schenkbarnabas.raspberrythermostat.exceptions;

public class TemperatureSensorNotFoundException extends RuntimeException {
    public TemperatureSensorNotFoundException() {
        super("Temperature sensor not found!");
    }
}
