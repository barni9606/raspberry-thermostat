package com.schenkbarnabas.raspberrythermostat.controllers;

import com.schenkbarnabas.raspberrythermostat.model.Day;
import com.schenkbarnabas.raspberrythermostat.services.TemperatureService;
import com.schenkbarnabas.raspberrythermostat.services.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/rs")
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MMMM dd. HH:mm");

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private WeekService weekService;

    @GetMapping("/local-time-and-temp")
    public Map<String, String> getTimeAndTemp() {
        Map<String, String> map = new HashMap<>();
        map.put("time", dateFormat.format(new Date()));
        map.put("temp", Float.toString(temperatureService.getCurrentTemp()));
        return map;
    }

    @GetMapping("/week")
    public List<Day> getWeek() {
        return WeekService.getCurrentWeek();
    }

    @PutMapping("/week")
    public String putWeek(@RequestBody List<Day> week) {
        WeekService.saveCurrentWeek(week);
        return "Ok";
    }
}
