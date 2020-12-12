package com.sprc.weatherapp.api.temperature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    @NotNull
    @JsonProperty(value = "city_id")
    private Long cityId;

    @NotNull
    private Double value;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getId() {
        return id;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
