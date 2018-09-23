package com.schenkbarnabas.raspberrythermostat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Time {
    private int hours;
    private int minutes;

    @JsonIgnore
    public int getTimeInt() {
        return hours * 60 + minutes;
    }

    @JsonIgnore
    public boolean isEndOfDay() {
        return hours == 24 && minutes == 0;
    }

    @JsonIgnore
    public boolean isStartOfDay() {
        return hours == 0 && minutes == 0;
    }
}
