package com.schenkbarnabas.raspberrythermostat.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WifiConfig {
    private String ssid;
    private String password;
}
