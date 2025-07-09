package com.weather.info.service;

import com.weather.info.entity.PincodeEntity;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;

public interface OpenWeatherService {
    OpenWeatherResponseVO getWeatherForPincode(PincodeEntity pincode);

    OpenWeatherCoordinatesResponseVO getCoordinatesForPincode(long pincode);
}
