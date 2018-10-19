package com.schenkbarnabas.raspberrythermostat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
@PropertySource("classpath:application.yml")
public class RaspberryThermostatApplication {
	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(RaspberryThermostatApplication.class, args);
	}

	public static void exit(String message, int exitCode) {
		log.error(message);
		ctx.close();
		System.exit(exitCode);
	}
}
