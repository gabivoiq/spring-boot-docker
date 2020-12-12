package com.sprc.weatherapp.api.city;

public class CityUtils {

    private CityUtils() {
        // utils class
    }

    public static String build404Message(Long id) {
        return "City with id " + id + " does not exist";
    }

    public static String build409Message(String cityName, Long countryId) {
        return "A city with name " + cityName + " and country id " + countryId + " already exists";
    }
}
