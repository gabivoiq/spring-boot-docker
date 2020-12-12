package com.sprc.weatherapp.api.city;

import com.sprc.weatherapp.api.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByName(@Param("name") String cityName);
    List<City> findAllByCountry(@Param("country") Country country);
    Optional<City> findByNameAndCountry(@Param("name") String name, @Param("country") Country country);
}
