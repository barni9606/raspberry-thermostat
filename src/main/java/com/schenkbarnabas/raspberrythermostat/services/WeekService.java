package com.schenkbarnabas.raspberrythermostat.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schenkbarnabas.raspberrythermostat.model.Day;
import com.schenkbarnabas.raspberrythermostat.model.Period;
import com.schenkbarnabas.raspberrythermostat.model.Time;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WeekService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Getter
    private static List<Day> currentWeek = new ArrayList<>();
    private static final String weekFilePathName = "week.json";

    static {
        loadCurrentWeek();
    }

    public static void saveCurrentWeek(List<Day> week) {
        try {
            currentWeek = week;
            objectMapper.writer().writeValue(new File(weekFilePathName), currentWeek);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCurrentWeek() {
        File file = new File(weekFilePathName);
        if (file.exists()) {
            try {
                currentWeek= objectMapper.readValue(file, new TypeReference<List<Day>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveCurrentWeek(getDefaultWeek());
        }
    }

    public static List<Day> getDefaultWeek() {
        List<Day> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Period period = new Period();
            period.setStart(new Time(0, 0));
            period.setFinish(new Time(24, 0));
            period.setTemperature(20.f);
            Day day = new Day();
            List<Period> periods = new ArrayList<>();
            periods.add(period);
            day.setPeriods(periods);
            week.add(day);
        }
        return week;
    }
}