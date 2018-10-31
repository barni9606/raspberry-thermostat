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
    private int hour;
    private int minute;

    @JsonIgnore
    public int getTimeInt() {
        return hour * 60 + minute;
    }

    @JsonIgnore
    public boolean isEndOfDay() {
        return hour == 24 && minute == 0;
    }

    @JsonIgnore
    public boolean isStartOfDay() {
        return hour == 0 && minute == 0;
    }
}
