package com.schenkbarnabas.raspberrythermostat.regulator;

import java.time.Duration;
import java.time.LocalDateTime;

public class LinearPredictor implements Predictor {
    private LocalDateTime predictionTimeStart;
    private float predictionTempStart;
    private float scalar = 10; // [minute / dC°]

    @Override
    public void startPredictionTime(float currentTemp) {
        if (predictionTimeStart == null) {
            predictionTimeStart = LocalDateTime.now();
            predictionTempStart = currentTemp;
        }
    }

    @Override
    public int predict(float currentTemp, float requiredTemp) {
        return (int) Math.floor(scalar * (requiredTemp - currentTemp));
    }

    @Override
    public void error(float currentTemp, float requiredTemp) {
        if (predictionTimeStart == null) {
            return;
        }

        float dT = currentTemp - predictionTempStart;
        long elapsedMinutes = Duration.between(predictionTimeStart, LocalDateTime.now()).toMinutes();
        if (dT > 0.5f && elapsedMinutes > 2) {
            scalar = 1.0f * elapsedMinutes / dT;
        } else if (dT < 0.f) {
            //TODO: HIBA
        }
        predictionTimeStart = null;
    }


}
