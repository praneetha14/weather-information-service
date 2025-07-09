package com.weather.info.service;

import com.weather.info.model.WeatherResponseVO;

public interface WeatherService {
    WeatherResponseVO getWeatherInformation(long pincode, String date);
}
