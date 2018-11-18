package com.schenkbarnabas.raspberrythermostat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schenkbarnabas.raspberrythermostat.exceptions.IncorrectProgramFormatException;
import com.schenkbarnabas.raspberrythermostat.exceptions.ProgramNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Program {
    private Day monday;
    private Day tuesday;
    private Day wednesday;
    private Day thursday;
    private Day friday;
    private Day saturday;
    private Day sunday;
    private long timestamp;

    @JsonIgnore
    public Program(List<Day> week) {
        if (week == null || week.size() != 7) {
            throw new IncorrectProgramFormatException(week);
        }
        monday    = week.get(0);
        tuesday   = week.get(1);
        wednesday = week.get(2);
        thursday  = week.get(3);
        friday    = week.get(4);
        saturday  = week.get(5);
        sunday    = week.get(6);
    }

    @JsonIgnore
    public Day get(int i) {
        switch (i) {
            case 0:
                return monday;
            case 1:
                return tuesday;
            case 2:
                return wednesday;
            case 3:
                return thursday;
            case 4:
                return friday;
            case 5:
                return saturday;
            case 6:
                return sunday;
            default:
                throw new ProgramNotFoundException(i);
        }
    }
}
