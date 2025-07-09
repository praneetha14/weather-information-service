package com.weather.info.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather.info.openweather")
@Getter
@Setter
public class OpenWeatherConfigProperties {
    private String appId;
}
