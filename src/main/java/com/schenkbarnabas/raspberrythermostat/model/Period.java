package com.schenkbarnabas.raspberrythermostat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {
    private Time start;
    private Time finish;
    private float temperature;
}
