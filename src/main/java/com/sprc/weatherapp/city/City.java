package com.sprc.weatherapp.city;

import com.sprc.weatherapp.country.Country;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="country_id", referencedColumnName = "id", Casca)
    private Country country;


}
