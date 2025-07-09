package com.weather.info.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "weather")
@Getter
@Setter
public class WeatherEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pin_code_id", referencedColumnName = "id")
    private PincodeEntity pincode;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "temperature")
    private double temperature;

    @Column(name = "weather_condition")
    private String condition;

    @Column(name = "description")
    private String description;

    @Column(name = "feels_like")
    private double feelsLike;

    @Column(name = "min_temp")
    private double minTemp;

    @Column(name = "max_temp")
    private double maxTemp;

    @Column(name = "pressure")
    private double pressure;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "wind_direction")
    private double windDirection;
}
