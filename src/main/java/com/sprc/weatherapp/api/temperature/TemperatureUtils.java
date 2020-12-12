package com.sprc.weatherapp.api.temperature;

public class TemperatureUtils {

    private TemperatureUtils() {
        // utils class
    }

    public static String build404Message(Long id) {
        return "Temperature with id " + id + " does not exist";
    }
}
