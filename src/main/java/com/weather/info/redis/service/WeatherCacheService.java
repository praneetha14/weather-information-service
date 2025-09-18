package com.weather.info.redis.service;

import com.weather.info.model.WeatherResponseVO;

import java.util.concurrent.TimeUnit;

public interface WeatherCacheService {
    void saveWeatherInfo(String key, WeatherResponseVO weatherResponseVO, long ttl, TimeUnit timeUnit);
    WeatherResponseVO getWeatherInfo(String key);
}
