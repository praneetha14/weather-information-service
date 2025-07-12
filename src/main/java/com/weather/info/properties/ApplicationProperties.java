package com.weather.info.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather.info.application")
@Getter
@Setter
public class ApplicationProperties {
    private String baseUrl;
}
