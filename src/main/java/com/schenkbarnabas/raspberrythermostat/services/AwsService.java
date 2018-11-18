package com.schenkbarnabas.raspberrythermostat.services;

import com.amazonaws.services.iot.client.*;
import com.amazonaws.services.iot.client.sample.sampleUtil.SampleUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.schenkbarnabas.raspberrythermostat.RaspberryThermostatApplication;
import com.schenkbarnabas.raspberrythermostat.configs.AwsConfig;
import com.schenkbarnabas.raspberrythermostat.model.Program;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Service
@Slf4j
public class AwsService {
    private String awsConfigFilePath = "./awsConfig.yml";

    private AwsConfig awsConfig;

    private AWSIotMqttClient client;
    private AWSIotDevice device;

    private final String topicName;
    private final AWSIotQos topicQos = AWSIotQos.QOS1;

    @Autowired
    private TemperatureService temperatureService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MMMM dd. HH:mm");

    public AwsService() {
        loadAwsConfig();

        topicName = awsConfig.getThingName() + "/program";

        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(awsConfig.getCertificateFile(), awsConfig.getPrivateKeyFile());
        client = new AWSIotMqttClient(awsConfig.getClientEndpoint(), awsConfig.getClientId(), pair.keyStore, pair.keyPassword);
        device = new AWSIotDevice(awsConfig.getThingName());

        AWSIotTopic topic = new MyTopic(topicName, topicQos);
        try {
            client.subscribe(topic, false);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }

        try {
            client.attach(device);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
        connect();
    }

    private void loadAwsConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            awsConfig = mapper.readValue(new File(awsConfigFilePath), AwsConfig.class);
        } catch (IOException e) {
            RaspberryThermostatApplication.exit("Couldn't find awsConf.yml", 1);
        }
    }

    private void connect() {
        if (client.getConnectionStatus() == AWSIotConnectionStatus.DISCONNECTED) {
            try {
                client.connect(5000);
                WeekService.saveCurrentWeek(JsonPath.parse(device.get())
                        .read("$.state.desired.program", Program.class));
            } catch (AWSIotException e) {
                e.printStackTrace();
            } catch (AWSIotTimeoutException e) {
                e.printStackTrace();
            } catch (PathNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedRate = 300000)
    private void updateShadow() throws AWSIotException {

        connect();

        // Update shadow document
        String state =
                "{\"state\":" +
                        "{\"reported\":" +
                            "{\"temp\": \""+ temperatureService.getCurrentTemp() +"\"," +
                            "\"time\": \""+ dateFormat.format(new Date()) +"\"}" +
                        "}" +
                "}";
        device.update(state);
    }
}
