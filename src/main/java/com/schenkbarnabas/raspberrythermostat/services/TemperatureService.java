package com.schenkbarnabas.raspberrythermostat.services;

import org.springframework.stereotype.Service;

@Service
public class TemperatureService {
    public float getCurrentTemp() {
        return 20.f;
    }
}
