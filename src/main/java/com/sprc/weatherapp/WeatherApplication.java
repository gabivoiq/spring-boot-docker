package com.sprc.weatherapp;

import com.sprc.weatherapp.api.city.CityRepository;
import com.sprc.weatherapp.api.country.CountryRepository;
import com.sprc.weatherapp.api.temperature.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApplication {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private CountryRepository countryRepository;

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }
}