package com.schenkbarnabas.raspberrythermostat.exceptions;

public class ProgramNotFoundException extends RuntimeException {
    public ProgramNotFoundException(int i) {
        super("Program not found: " + i);
    }
}
