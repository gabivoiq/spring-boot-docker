package com.sprc.weatherapp.api.temperature;

import com.sprc.weatherapp.api.city.City;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "temperatures", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"city_id", "timestamp"})
})
public class Temperature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name="city_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;

    @NotNull
    private Double value;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", city=" + city +
                ", value=" + value +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
