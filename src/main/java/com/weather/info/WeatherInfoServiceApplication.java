package com.weather.info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EntityScan(basePackages = "com.weather.info.entity")
public class WeatherInfoServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(WeatherInfoServiceApplication.class, args);
    }

}
