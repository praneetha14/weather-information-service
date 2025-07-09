package com.weather.info.service;

import com.weather.info.entity.PincodeEntity;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;

/**
 *  OpenWeatherService is a interface and acts as a service layer to handle business logic while calling OpenWeather
 *   APIs via OpenWeatherClient
 */
public interface OpenWeatherService {


    /**
     *  This method is responsible to fetch weather information by taking pincode as parameter.
     *
     * @param pincode Pincode represents the zip code for which the weather info needs to be fetched
     * @return weather information for a given pin code
     */
    OpenWeatherResponseVO getWeatherForPincode(PincodeEntity pincode);

    /**
     * This method is responsible to fetch Geo-Spatial coordinates data by calling OpenWeather API using
     * OpenWeatherClient
     *
     * @param pincode pincode represents the pincode for which the Geo-Spatial coordinates data to be fetched.
     * @return Geo-Spatial coordinate data which include latitude, longitude values for a given pincode
     */
    OpenWeatherCoordinatesResponseVO getCoordinatesForPincode(long pincode);
}
