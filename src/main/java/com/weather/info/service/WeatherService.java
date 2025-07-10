package com.weather.info.service;

import com.weather.info.model.WeatherResponseVO;

/**
 *      WeatherService is a interface for the service layer of this application.
 *      This class is responsible to fetch weather information from OpenWeather API and store it in RDBMS.
 */


public interface WeatherService {

    /**
     * This method is responsible to fetch weather information from OpenWeatherAPI and store it in RDBMS.
     * This method also performs internal sanity checks on the given input parameters.
     * Returns Weather information from db if already exists in db or fetches, stores and returns the information by
     * calling OpenWeatherAPI.
     *
     *
     * @param pincode pincode represents the zip code for which the weather information needs to be fetched
     * @param date date represents the data for which the weather information needs to be fetched.
     * @return Weather information for a pincode for a particular date.
     */

    WeatherResponseVO getWeatherInformation(long pincode, String date);
}
