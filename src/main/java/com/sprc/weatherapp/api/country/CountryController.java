package com.sprc.weatherapp.api.country;

import com.sprc.weatherapp.api.exceptions.ConflictException;
import com.sprc.weatherapp.api.exceptions.ResourceNotFoundException;
import com.sprc.weatherapp.api.exceptions.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sprc.weatherapp.api.country.CountryUtils.build404Message;
import static com.sprc.weatherapp.api.country.CountryUtils.build409Message;

@RestController
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@RequestBody Country country) {
        Optional<Country> countryOptional = countryRepository.findByName(country.getName());
        if (countryOptional.isPresent()) {
            throw new ConflictException(build409Message(country.getName()));
        }
        final Country savedCountry = countryRepository.save(country);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", savedCountry.getId()));
    }

    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> countries = countryRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(countries);
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<ResponseMessage> updateCountry(@PathVariable("id") Long id, @RequestBody Country country) {
        if (country.getId() == null || !country.getId().equals(id)) {
            throw new IllegalArgumentException();
        }
        Optional<Country> countryOptional = countryRepository.findById(id);
        if (countryOptional.isEmpty()) {
            throw new ResourceNotFoundException(build404Message(id));
        }
        Country countryDb = countryOptional.get();
        Optional<Country> countryWithSameName = countryRepository.findByName(country.getName());
        if (countryWithSameName.isPresent() && !countryDb.getId().equals(countryWithSameName.get().getId())) {
            throw new ConflictException(build409Message(country.getName()));
        }
        countryDb.setName(country.getName());
        countryDb.setLatitude(country.getLatitude());
        countryDb.setLongitude(country.getLongitude());
        countryRepository.save(countryDb);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Country with id " + id + " was updated successfully"));
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<ResponseMessage> deleteCountry(@PathVariable("id") Long id) {
        Optional<Country> countryDb = countryRepository.findById(id);
        if (countryDb.isEmpty()) {
            throw new ResourceNotFoundException(build404Message(id));
        }
        countryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Country with id " + id + " was deleted successfully"));
    }

}
