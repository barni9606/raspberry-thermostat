package com.schenkbarnabas.raspberrythermostat.services;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schenkbarnabas.raspberrythermostat.model.Program;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class MyTopic extends AWSIotTopic {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        try {
            WeekService.saveCurrentWeek(objectMapper.readValue(message.getStringPayload(), Program.class));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Beolvasás sikeres: " + message.getStringPayload());
    }
}