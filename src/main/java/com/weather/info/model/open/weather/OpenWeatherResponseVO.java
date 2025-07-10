package com.weather.info.model.open.weather;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OpenWeatherResponseVO {

    private TemperatureResponse main;
    private List<WeatherMainResponse> weather;
    private WindData wind;

    @Getter
    @Setter
    public static class WindData {
        private double speed;
        private double deg;
    }

    @Getter
    @Setter
    public static class TemperatureResponse {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private double pressure;
        private double humidity;
        private double sea_level;
        private double grnd_level;
    }

    @Getter
    @Setter
    public static class WeatherMainResponse {
        private long id;
        private String main;
        private String description;
        private String icon;
    }

}
