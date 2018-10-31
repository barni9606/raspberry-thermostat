package com.schenkbarnabas.raspberrythermostat.exceptions;

import com.schenkbarnabas.raspberrythermostat.model.Day;

import java.util.List;

public class IncorrectProgramFormatException extends RuntimeException {
    public IncorrectProgramFormatException(List<Day> week) {
        super("Incorrect program provided!" + week);
    }
}
