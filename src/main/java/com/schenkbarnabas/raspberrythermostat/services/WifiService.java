package com.schenkbarnabas.raspberrythermostat.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
public class WifiService {
    public static void setNetwork(WifiConfig wifiConfig) {
        if (wifiConfig == null) {
            throw new RuntimeException("WifiConfig cannot be null");
        }
        String network;
        if (!"".equals(wifiConfig.getPassword())) {
            network = "\nnetwork={\n" +
                    "    ssid=\""+ wifiConfig.getSsid() + "\"\n" +
                    "    psk=\"" + wifiConfig.getPassword() + "\"\n" +
                    "}\n";
        } else {
            network = "\nnetwork={\n" +
                    "    ssid=\""+ wifiConfig.getSsid() + "\"\n" +
                    "    key_mgmt=NONE\n" +
                    "}\n";
        }
        try {
            Files.write(Paths.get("/etc/wpa_supplicant/wpa_supplicant.conf"),
                    network.getBytes(), StandardOpenOption.APPEND);
            log.info(network);
            Runtime.getRuntime().exec("wpa_cli -i wlan0 reconfigure");
        }catch (IOException e) {
            log.error(e.toString());
        }
    }

    public static boolean isWirelessNetworkUsed() {
        String network = "";
        try {
            Process proc = Runtime.getRuntime().exec("route");
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));
            BufferedReader errInput = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("default")) {
                    network = s;
                    break;
                }
            }

            stdInput.close();
            errInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return network.contains("wlan0");
    }
}
