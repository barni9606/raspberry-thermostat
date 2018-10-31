package com.schenkbarnabas.raspberrythermostat.services;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schenkbarnabas.raspberrythermostat.model.Program;

import java.io.IOException;

public class MyTopic extends AWSIotTopic {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        try {
            WeekService.saveCurrentWeek(objectMapper.readValue(message.getStringPayload(), Program.class));
            System.out.println("Beolvasás sikeres: " + message.getStringPayload());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nem sikerült beolvasni: " + message.getStringPayload());
        }
    }
}