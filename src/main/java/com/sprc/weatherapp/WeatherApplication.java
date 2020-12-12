package com.sprc.weatherapp;

import com.sprc.weatherapp.api.city.City;
import com.sprc.weatherapp.api.city.CityRepository;
import com.sprc.weatherapp.api.country.Country;
import com.sprc.weatherapp.api.country.CountryRepository;
import com.sprc.weatherapp.api.temperature.Temperature;
import com.sprc.weatherapp.api.temperature.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

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

//    @EventListener(ApplicationReadyEvent.class)
//    public void writeToDb() {
//        Country country = new Country();
//        country.setLatitude(1231.1);
//        country.setLongitude(12312.32);
//        country.setName("aaaaaa");
//
//        countryRepository.save(country);
//        List<Country> list = countryRepository.findAll();
//
//        City city = new City();
//        city.setLatitude(123.1);
//        city.setLongitude(123.1);
//        city.setCountry(list.get(0));
//
//        cityRepository.save(city);
//
//        Temperature temp = new Temperature();
//        temp.setLongitude(12.3);
//        temp.setLatitude(231.11);
//        temp.setCity(cityRepository.findAll().get(0));
//
//        temperatureRepository.save(temp);
//        Temperature tempss = temperatureRepository.findAll().get(0);
//        System.out.println(tempss);
//    }
}