package com.sprc.weatherapp.api.country;

public class CountryUtils {

    private CountryUtils() {
        // utils class
    }

    public static String build404Message(Long id) {
        return "Country with id " + id + " does not exist";
    }

    public static String build409Message(String countryName) {
        return "A country with name " + countryName + " already exists";
    }
}
