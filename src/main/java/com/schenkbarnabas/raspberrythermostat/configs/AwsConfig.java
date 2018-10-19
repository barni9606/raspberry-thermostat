package com.schenkbarnabas.raspberrythermostat.configs;

import lombok.*;

@Getter
@Setter
public class AwsConfig {
    private String prefix;
    private String clientId;
    private String region;
    private String certificateFile;
    private String privateKeyFile;
    private String thingName;

    public String getClientEndpoint() {
        return prefix + ".iot." + region + ".amazonaws.com";
    }
}
