package com.sprc.weatherapp.api.temperature;

import com.sprc.weatherapp.api.city.City;
import com.sprc.weatherapp.api.city.CityRepository;
import com.sprc.weatherapp.api.city.CityUtils;
import com.sprc.weatherapp.api.exceptions.ResourceNotFoundException;
import com.sprc.weatherapp.api.exceptions.ResponseMessage;
import com.sprc.weatherapp.api.utils.DateUtils;
import com.sprc.weatherapp.api.utils.TypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class TemperatureController {

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private CityRepository cityRepository;

    @PostMapping("/temperatures")
    public ResponseEntity<Object> createTemperature(@RequestBody Temperature temperature) {
        Optional<City> cityOptional = cityRepository.findById(temperature.getCityId());
        if (cityOptional.isEmpty()) {
            throw new ResourceNotFoundException(CityUtils.build404Message(temperature.getCityId()));
        }
        temperature.setCity(cityOptional.get());
        final Temperature savedTemperature = temperatureRepository.save(temperature);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", savedTemperature.getId()));
    }

    @GetMapping("/temperatures")
    public ResponseEntity<List<Temperature>> getTemperaturesFilters(@RequestParam(value = "lat", required = false) String latitudeStr,
                                                                    @RequestParam(value = "lon", required = false) String longitudeStr,
                                                                    @RequestParam(value = "from", required = false) String fromDateStr,
                                                                    @RequestParam(value = "until", required = false) String untilDateStr) {
        if (!DateUtils.isValidFormat(fromDateStr) || !DateUtils.isValidFormat(untilDateStr) ||
                !TypeUtils.isValidDouble(latitudeStr) || !TypeUtils.isValidDouble(longitudeStr)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        final Double latitude = latitudeStr != null ? Double.parseDouble(latitudeStr) : null;
        final Double longitude = longitudeStr != null ? Double.parseDouble(longitudeStr) : null;
        final LocalDateTime fromDateTime = fromDateStr != null ? DateUtils.convertStringToLocalDate(fromDateStr).atStartOfDay() : null;
        final LocalDateTime untilDateTime = untilDateStr != null ? DateUtils.convertStringToLocalDate(untilDateStr).atTime(LocalTime.MAX) : null;

        List<Temperature> temperatures = temperatureRepository.findAllTempsBetweenDates(fromDateTime, untilDateTime);

        if (latitude != null) {
            temperatures = temperatures
                    .stream()
                    .filter(t -> t.getCity().getLatitude().equals(latitude))
                    .collect(Collectors.toList());
        }
        if (longitude != null) {
            temperatures = temperatures
                    .stream()
                    .filter(t -> t.getCity().getLongitude().equals(longitude))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(temperatures);
    }

    @GetMapping("/temperatures/cities/{city_id}")
    public ResponseEntity<List<Temperature>> getTemperaturesFiltersCity(@PathVariable("city_id") String cityIdStr,
                                                                        @RequestParam(value = "from", required = false) String fromDateStr,
                                                                        @RequestParam(value = "until", required = false) String untilDateStr) {

        if (!TypeUtils.isValidLong(cityIdStr) || !DateUtils.isValidFormat(fromDateStr) || !DateUtils.isValidFormat(untilDateStr)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        Long cityId = Long.parseLong(cityIdStr);
        Optional<City> city = cityRepository.findById(cityId);
        if (city.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }

        final LocalDateTime fromDateTime = fromDateStr != null ? DateUtils.convertStringToLocalDate(fromDateStr).atStartOfDay() : null;
        final LocalDateTime untilDateTime = untilDateStr != null ? DateUtils.convertStringToLocalDate(untilDateStr).atTime(LocalTime.MAX) : null;

        List<Temperature> temperatures = temperatureRepository.findAllTempsCityBetweenDates(city.get(), fromDateTime, untilDateTime);
        return ResponseEntity.status(HttpStatus.OK).body(temperatures);
    }

    @GetMapping("/temperatures/countries/{country_id}")
    public ResponseEntity<List<Temperature>> getTemperaturesFiltersCountry(@PathVariable("country_id") String countryIdStr,
                                                                           @RequestParam(value = "from", required = false) String fromDateStr,
                                                                           @RequestParam(value = "until", required = false) String untilDateStr) {
        if (!TypeUtils.isValidLong(countryIdStr) || !DateUtils.isValidFormat(fromDateStr) || !DateUtils.isValidFormat(untilDateStr)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        Long countryId = Long.parseLong(countryIdStr);
        List<Temperature> temperatures = temperatureRepository.findAllTempsCountryBetweenDates(countryId, fromDateStr, untilDateStr);
        return ResponseEntity.status(HttpStatus.OK).body(temperatures);
    }

    @PutMapping("/temperatures/{id}")
    public ResponseEntity<ResponseMessage> updateTemperature(@PathVariable("id") Long id, @RequestBody Temperature temperature) {
        if (temperature.getId() == null || !temperature.getId().equals(id)) {
            throw new IllegalArgumentException();
        }
        Optional<Temperature> temperatureOptional = temperatureRepository.findById(id);
        if (temperatureOptional.isEmpty()) {
            throw new ResourceNotFoundException(TemperatureUtils.build404Message(id));
        }
        Optional<City> cityOptional = cityRepository.findById(temperature.getCityId());
        if (cityOptional.isEmpty()) {
            throw new ResourceNotFoundException(CityUtils.build404Message(id));
        }
        Temperature temperatureDb = temperatureOptional.get();

        temperatureDb.setCity(cityOptional.get());
        temperatureDb.setValue(temperature.getValue());
        temperatureDb.setCityId(temperature.getCityId());

        temperatureRepository.save(temperatureDb);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Temperature with id " + id + " was updated successfully"));
    }

    @DeleteMapping("/temperatures/{id}")
    public ResponseEntity<ResponseMessage> deleteTemperature(@PathVariable("id") Long id) {
        Optional<Temperature> temperatureDb = temperatureRepository.findById(id);
        if (temperatureDb.isEmpty()) {
            throw new ResourceNotFoundException(TemperatureUtils.build404Message(id));
        }
        temperatureRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Temperature with id " + id + " was deleted successfully"));
    }
}
