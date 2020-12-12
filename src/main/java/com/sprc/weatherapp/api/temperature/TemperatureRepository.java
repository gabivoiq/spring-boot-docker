package com.sprc.weatherapp.api.temperature;

import com.sprc.weatherapp.api.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TemperatureRepository extends JpaRepository<Temperature, Long> {

    @Query("SELECT t FROM Temperature t WHERE" +
            "(CAST(:from as timestamp) is null or t.timestamp >= :from) and " +
            "(CAST(:until as timestamp) is null or t.timestamp <= :until)")
    List<Temperature> findAllTempsBetweenDates(@Param("from") LocalDateTime from, @Param("until") LocalDateTime until);

    @Query("SELECT t from Temperature t WHERE t.city = :city and " +
            "(CAST(:from as timestamp) is null or t.timestamp >= :from) and " +
            "(CAST(:until as timestamp) is null or t.timestamp <= :until)")
    List<Temperature> findAllTempsCityBetweenDates(@Param("city") City city, @Param("from") LocalDateTime from, @Param("until") LocalDateTime until);

    @Query(value =
            "select * from temperatures\n" +
                    "where city_id in\n" +
                    "    (select id from cities\n" +
                    "    where country_id = :country_id)\n" +
                    "and (:from is null or timestamp >= to_date(cast(:from as TEXT), 'yyyy-MM-dd'))\n" +
                    "and (:until is null or timestamp < to_date(cast(:until as TEXT), 'yyyy-MM-dd') + INTERVAL '1 DAY')\n",
            nativeQuery = true)
    List<Temperature> findAllTempsCountryBetweenDates(@Param("country_id") Long countryId,
                                                      @Param("from") String fromDate,
                                                      @Param("until") String untilDate);
}
