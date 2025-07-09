package com.weather.info.model.open.weather;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenWeatherCoordinatesResponseVO {
    private String zip;
    private String name;
    private double lat;
    private double lon;
    private String country;
}
