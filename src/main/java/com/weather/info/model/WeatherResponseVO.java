package com.weather.info.model;

import java.io.Serializable;
import java.time.LocalDate;

public record WeatherResponseVO(LocalDate date, long pincode, double temperature, String condition, String description,
                                double feelsLike, double minimumTemperature, double maximumTemperature, double pressure,
                                double humidity, double windSpeed, double windDirection) implements Serializable {
}
