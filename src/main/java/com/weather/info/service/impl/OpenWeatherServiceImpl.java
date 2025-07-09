package com.weather.info.service.impl;

import com.weather.info.client.OpenWeatherClient;
import com.weather.info.entity.PincodeEntity;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;
import com.weather.info.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

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