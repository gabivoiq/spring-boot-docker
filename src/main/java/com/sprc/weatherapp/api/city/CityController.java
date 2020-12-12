package com.sprc.weatherapp.api.city;

import com.sprc.weatherapp.api.country.Country;
import com.sprc.weatherapp.api.country.CountryRepository;
import com.sprc.weatherapp.api.country.CountryUtils;
import com.sprc.weatherapp.api.exceptions.ConflictException;
import com.sprc.weatherapp.api.exceptions.ResourceNotFoundException;
import com.sprc.weatherapp.api.exceptions.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sprc.weatherapp.api.country.CountryUtils.build404Message;

@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @PostMapping("/cities")
    public ResponseEntity<Object> createCity(@RequestBody City city) {
        Optional<Country> countryOptional = countryRepository.findById(city.getCountryId());
        if (countryOptional.isEmpty()) {
            throw new ResourceNotFoundException(CountryUtils.build404Message(city.getCountryId()));
        }
        Optional<City> cityOptional = cityRepository.findByNameAndCountry(city.getName(), countryOptional.get());
        if (cityOptional.isPresent()) {
            throw new ConflictException(CityUtils.build409Message(city.getName(), city.getCountryId()));
        }
        city.setCountry(countryOptional.get());
        final City savedCity = cityRepository.save(city);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", savedCity.getId()));
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getCities() {
        List<City> cities = cityRepository.findAll();
        cities.forEach(city -> city.setCountryId(city.getCountry().getId()));
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    @GetMapping("/cities/country/{country_id}")
    public ResponseEntity<List<City>> getCitiesFromCountry(@PathVariable("country_id") Long countryId) {
        Optional<Country> countryOptional = countryRepository.findById(countryId);
        if (countryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
        }
        List<City> cities = cityRepository.findAllByCountry(countryOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(cities);
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<ResponseMessage> updateCity(@PathVariable("id") Long id, @RequestBody City city) {
        if (city.getId() == null || !city.getId().equals(id)) {
            throw new IllegalArgumentException();
        }
        Optional<City> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) {
            throw new ResourceNotFoundException(CityUtils.build404Message(id));
        }
        Optional<Country> countryOptional = countryRepository.findById(city.getCountryId());
        if (countryOptional.isEmpty()) {
            throw new ResourceNotFoundException(CountryUtils.build404Message(city.getCountryId()));
        }
        City cityDb = cityOptional.get();
        Optional<City> cityWithSameNameAndCountryId = cityRepository.findByNameAndCountry(city.getName(), countryOptional.get());
        if (cityWithSameNameAndCountryId.isPresent() && !cityDb.getId().equals(cityWithSameNameAndCountryId.get().getId())) {
            throw new ConflictException(CityUtils.build409Message(city.getName(), city.getCountryId()));
        }
        city.setCountry(countryOptional.get());

        cityDb.setName(city.getName());
        cityDb.setLatitude(city.getLatitude());
        cityDb.setLongitude(city.getLongitude());
        cityDb.setCountry(city.getCountry());
        cityDb.setCountryId(city.getCountryId());
        cityRepository.save(cityDb);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("City with id " + id + " was updated successfully"));
    }

    @DeleteMapping("/cities/{id}")
    public ResponseEntity<ResponseMessage> deleteCity(@PathVariable("id") Long id) {
        Optional<City> cityDb = cityRepository.findById(id);
        if (cityDb.isEmpty()) {
            throw new ResourceNotFoundException(build404Message(id));
        }
        cityRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("City with id " + id + " was deleted successfully"));
    }
}
