package com.weather.info.service.impl;

import com.weather.info.client.OpenWeatherClient;
import com.weather.info.entity.PincodeEntity;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;
import com.weather.info.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;

/**
 *  OpenWeatherServiceImpl is a concrete implementation of OpenWeatherService interface and provides the implementation
 *  of all the methods of this interface.
 */
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

    /**
     *  openWeatherClient is a bean of type OpenWeatherClient which is used to call methods of OpenWeatherClient
     *  which internally calls OpenWeather API to fetch weather information.
     */
    private final OpenWeatherClient openWeatherClient;

    @Override
    public OpenWeatherResponseVO getWeatherForPincode(PincodeEntity pincode) {
        return openWeatherClient.getWeatherForCoordinates(pincode.getCoordinatesEntity());
    }

    @Override
    public OpenWeatherCoordinatesResponseVO getCoordinatesForPincode(long pincode) {
        return openWeatherClient.fetchCoordinatesForZipCode(String.valueOf(pincode));
    }
}