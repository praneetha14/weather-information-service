package com.weather.info.model.open.weather.utils;

public final class OpenWeatherEndpoints {

    private OpenWeatherEndpoints() {
        throw new IllegalStateException("Utility classes cannot be instantiated");
    }

    public static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static final String GEO_CODING_URL = "http://api.openweathermap.org/geo/1.0/zip";
}
