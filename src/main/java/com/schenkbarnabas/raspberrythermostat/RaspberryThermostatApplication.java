package com.schenkbarnabas.raspberrythermostat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RaspberryThermostatApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaspberryThermostatApplication.class, args);
	}
}
