package com.weather.info.model;

public record WeatherResponseVO(long pincode, double temperature, String condition, String description,
                                double feelsLike, double minimumTemperature, double maximumTemperature, double pressure,
                                double humidity, double windSpeed, double windDirection) {
}
