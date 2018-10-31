package com.schenkbarnabas.raspberrythermostat.regulator;

/**
 * Predicts how much time it takes to heat the flat from
 * currentTemp to requiredTemp.
 *
 * If it fails, it should learn.
 */
public interface Predictor {
    /**
     * Starts the prediction time.
     * @param currentTemp current temperature
     */
    void startPredictionTime(float currentTemp);

    /**
     * Called when the next period has a higher temperature than the current one.
     * @param currentTemp current temperature
     * @param requiredTemp required temperature
     * @return predicted time (in minute) needed to get to the required temperature
     */
    int predict(float currentTemp, float requiredTemp);

    /**
     * Called when the system is stagnating.
     * Should fit prediction when first called after prediction time.
     * @param currentTemp current temperature
     * @param requiredTemp required temperature
     */
    void error( float currentTemp, float requiredTemp);
}
