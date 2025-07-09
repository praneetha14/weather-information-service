package com.weather.info;

import com.weather.info.entity.CoordinatesEntity;
import com.weather.info.entity.PincodeEntity;
import com.weather.info.exception.ResourceNotFoundException;
import com.weather.info.model.WeatherResponseVO;
import com.weather.info.model.enums.SupportedDatePatterns;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;
import com.weather.info.repository.PincodeRepository;
import com.weather.info.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class WeatherServiceImplTest extends AbstractTest {

    private static final String INVALID_DATE_ERROR_MESSAGE = "Please, provide a valid date";
    private static final String WEATHER_DATA_NOT_FOUND_MESSAGE = "Weather details not found for date: ";
    private static final String INVALID_DATE_FORMAT_ERROR_MESSAGE = "Invalid date: %s. Supported Date Formats are %s";
    private static final String FETCHING_COORDINATES_ERROR_MESSAGE = "Error occurred while fetching Coordinates."
            + "Recheck your pincode and try again";
    private static final String FETCHING_WEATHER_ERROR_MESSAGE = "Error occurred while fetching weather info.";

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private PincodeRepository pincodeRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void getWeatherInformationSuccessTest() {
        long pincode = 122017;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().format(formatter);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherCoordinatesResponseVO.class)))
                .thenReturn(createOpenWeatherCoordinatesResponseVO());
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponseVO.class)))
                .thenReturn(createOpenWeatherResponseVO());
        WeatherResponseVO weatherResponseVO = weatherService.getWeatherInformation(pincode, date);
        assertNotNull(weatherResponseVO);
    }

    @Test
    void getWeatherInformationForExistingPincodeSuccessTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().format(formatter);
        PincodeEntity pincodeEntity = getPincodeEntity();
        CoordinatesEntity coordinatesEntity = getCoordinatesEntity();
        pincodeEntity.setCoordinatesEntity(coordinatesEntity);
        pincodeRepository.save(pincodeEntity);

        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponseVO.class)))
                .thenReturn(createOpenWeatherResponseVO());

        WeatherResponseVO weatherResponseVO = weatherService.getWeatherInformation(pincodeEntity.getPincode(), date);
        assertNotNull(weatherResponseVO);
    }

    @Test
    void getWeatherInformationForExistingWeatherSuccessTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().format(formatter);
        PincodeEntity pincodeEntity = getPincodeEntity();
        CoordinatesEntity coordinatesEntity = getCoordinatesEntity();
        pincodeEntity.setCoordinatesEntity(coordinatesEntity);
        pincodeRepository.save(pincodeEntity);
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponseVO.class)))
                .thenReturn(createOpenWeatherResponseVO());
        weatherService.getWeatherInformation(pincodeEntity.getPincode(), date);
        WeatherResponseVO weatherResponseVO = weatherService.getWeatherInformation(pincodeEntity.getPincode(), date);
        assertNotNull(weatherResponseVO);
    }

    @Test
    void getWeatherInformationWithFutureDateFailureTest() {
        long pincode = 122017;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().plusDays(1).format(formatter);
        Throwable exception = assertThrows(RuntimeException.class,
                () -> weatherService.getWeatherInformation(pincode, date));
        assertNotNull(exception);
        assertEquals(INVALID_DATE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getWeatherInformationWithNotExistingDateFailureTest() {
        PincodeEntity pincodeEntity = getPincodeEntity();
        CoordinatesEntity coordinatesEntity = getCoordinatesEntity();
        pincodeEntity.setCoordinatesEntity(coordinatesEntity);
        pincodeRepository.save(pincodeEntity);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDate.now().minusDays(1).format(formatter);
        Throwable exception = assertThrows(ResourceNotFoundException.class,
                () -> weatherService.getWeatherInformation(pincodeEntity.getPincode(), date));
        assertNotNull(exception);
        assertEquals(WEATHER_DATA_NOT_FOUND_MESSAGE + LocalDate.now().minusDays(1),
                exception.getMessage());
    }

    @Test
    void getWeatherInformationWithInvalidDateFormatFailureTest() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        long pincode = 122017;
        Throwable exception = assertThrows(RuntimeException.class,
                () -> weatherService.getWeatherInformation(pincode, date));
        assertNotNull(exception);
        assertEquals(String.format(INVALID_DATE_FORMAT_ERROR_MESSAGE, date, SupportedDatePatterns.getSupportedDateRegex()),
                exception.getMessage());
    }

    @Test
    void getWeatherInformationWithErrorWhileCallingCoordinateAPIFailureTest() {
        long pincode = 122017;
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherCoordinatesResponseVO.class)))
                .thenThrow(new RuntimeException("Error while fetching coordinates"));
        Throwable exception = assertThrows(RuntimeException.class,
                () -> weatherService.getWeatherInformation(pincode, date));
        assertNotNull(exception);
        assertEquals(FETCHING_COORDINATES_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getWeatherInformationWithErrorWhileCallingWeatherAPIFailureTest() {
        long pincode = 122017;
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherCoordinatesResponseVO.class)))
                .thenReturn(createOpenWeatherCoordinatesResponseVO());
        when(restTemplate.getForObject(anyString(), eq(OpenWeatherResponseVO.class)))
                .thenThrow(new RuntimeException("Error while fetching weather"));
        Throwable exception = assertThrows(RuntimeException.class,
                () -> weatherService.getWeatherInformation(pincode, date));
        assertNotNull(exception);
        assertEquals(FETCHING_WEATHER_ERROR_MESSAGE, exception.getMessage());
    }

    private OpenWeatherResponseVO createOpenWeatherResponseVO() {
        OpenWeatherResponseVO responseVO = new OpenWeatherResponseVO();
        responseVO.setMain(getTemperatureResponseVO());
        responseVO.setWeather(List.of(getWeatherInformationResponseVO()));
        responseVO.setWind(getWeatherInformationResponseVOWindData());
        return responseVO;
    }

    private OpenWeatherCoordinatesResponseVO createOpenWeatherCoordinatesResponseVO() {
        OpenWeatherCoordinatesResponseVO responseVO = new OpenWeatherCoordinatesResponseVO();
        responseVO.setCountry("IN");
        responseVO.setLat(177.2);
        responseVO.setLon(89.4);
        return responseVO;
    }

    private PincodeEntity getPincodeEntity() {
        PincodeEntity pincodeEntity = new PincodeEntity();
        pincodeEntity.setPincode(122017);
        pincodeEntity.setPlace("Gurugram");
        pincodeEntity.setCountry("India");
        return pincodeEntity;
    }

    private CoordinatesEntity getCoordinatesEntity() {
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        coordinatesEntity.setLatitude(121.0);
        coordinatesEntity.setLongitude(122.0);
        return coordinatesEntity;
    }

    private OpenWeatherResponseVO.TemperatureResponse getTemperatureResponseVO() {
        OpenWeatherResponseVO.TemperatureResponse temperatureResponse = new OpenWeatherResponseVO.TemperatureResponse();
        temperatureResponse.setTemp(273.15);
        temperatureResponse.setTemp_min(273.15);
        temperatureResponse.setTemp_max(273.15);
        temperatureResponse.setHumidity(273.15);
        temperatureResponse.setFeels_like(273.15);
        temperatureResponse.setGrnd_level(273.15);
        temperatureResponse.setPressure(273.15);
        temperatureResponse.setSea_level(273.15);
        return temperatureResponse;
    }

    private OpenWeatherResponseVO.WeatherMainResponse getWeatherInformationResponseVO() {
        OpenWeatherResponseVO.WeatherMainResponse weatherInformationResponse = new OpenWeatherResponseVO.WeatherMainResponse();
        weatherInformationResponse.setMain("Cloudy");
        weatherInformationResponse.setDescription("Partly Cloudy");
        weatherInformationResponse.setIcon("Cloudy");
        return weatherInformationResponse;
    }

    private OpenWeatherResponseVO.WindData getWeatherInformationResponseVOWindData() {
        OpenWeatherResponseVO.WindData windData = new OpenWeatherResponseVO.WindData();
        windData.setSpeed(273.15);
        windData.setDeg(273.15);
        return windData;
    }
}
