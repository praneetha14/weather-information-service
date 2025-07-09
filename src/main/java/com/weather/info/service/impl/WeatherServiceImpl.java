package com.weather.info.service.impl;

import com.weather.info.entity.CoordinatesEntity;
import com.weather.info.entity.PincodeEntity;
import com.weather.info.entity.WeatherEntity;
import com.weather.info.exception.ResourceNotFoundException;
import com.weather.info.model.WeatherResponseVO;
import com.weather.info.model.enums.SupportedDatePatterns;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;
import com.weather.info.repository.PincodeRepository;
import com.weather.info.repository.WeatherRepository;
import com.weather.info.service.OpenWeatherService;
import com.weather.info.service.WeatherService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final PincodeRepository pincodeRepository;
    private final OpenWeatherService openWeatherService;

    private static double kelvinToDegree(double temperature) {
        return temperature - 273.15;
    }

    @Override
    public WeatherResponseVO getWeatherInformation(long pincode, String date) {
        LocalDate parsedDate = parseDate(date);
        validateDate(parsedDate);
        Optional<PincodeEntity> pincodeEntity = pincodeRepository.findByPincode(pincode);
        if (pincodeEntity.isPresent()) {
            Optional<WeatherEntity> weatherEntity = weatherRepository.findByPincodeAndDate(pincodeEntity.get(),
                    parsedDate);
            return weatherEntity.map(entity -> toWeatherResponseVO(entity, pincode))
                    .orElseGet(() -> {
                        verifyDate(parsedDate);
                        return fetchAndSaveWeatherInformation(pincodeEntity.get());
                    });
        }
        verifyDate(parsedDate);
        PincodeEntity pincodeEntityWithCoordinates = createPincodeWithCoordinates(pincode);
        return fetchAndSaveWeatherInformation(pincodeEntityWithCoordinates);
    }

    private void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new RuntimeException("Please, provide a valid date");
        }
    }

    private void verifyDate(LocalDate date) {
        if (!date.isEqual(LocalDate.now())) {
            throw new ResourceNotFoundException("Weather details not found for date: " + date);
        }
    }

    private WeatherResponseVO fetchAndSaveWeatherInformation(PincodeEntity pincodeEntity) {
        OpenWeatherResponseVO openWeatherResponseVO = openWeatherService.getWeatherForPincode(pincodeEntity);
        WeatherEntity weatherEntity = toEntity(openWeatherResponseVO);
        weatherEntity.setPincode(pincodeEntity);
        weatherRepository.save(weatherEntity);
        return toWeatherResponseVO(weatherEntity, pincodeEntity.getPincode());
    }

    private WeatherResponseVO toWeatherResponseVO(WeatherEntity weatherEntity, long pincode) {
        return new WeatherResponseVO(pincode, weatherEntity.getTemperature(), weatherEntity.getCondition(),
                weatherEntity.getDescription(), weatherEntity.getFeelsLike(), weatherEntity.getMinTemp(),
                weatherEntity.getMaxTemp(), weatherEntity.getPressure(), weatherEntity.getHumidity(),
                weatherEntity.getWindSpeed(), weatherEntity.getWindDirection());
    }

    private WeatherEntity toEntity(OpenWeatherResponseVO openWeatherResponseVO) {
        WeatherEntity weatherEntity = new WeatherEntity();
        if (openWeatherResponseVO.getWeather() != null) {
            weatherEntity.setCondition(openWeatherResponseVO.getWeather().get(0).getMain());
            weatherEntity.setDescription(openWeatherResponseVO.getWeather().get(0).getDescription());
        }
        weatherEntity.setDate(LocalDate.now());
        if (openWeatherResponseVO.getMain() != null) {
            weatherEntity.setTemperature(openWeatherResponseVO.getMain().getTemp());
            weatherEntity.setFeelsLike(openWeatherResponseVO.getMain().getFeels_like());
            weatherEntity.setMinTemp(openWeatherResponseVO.getMain().getTemp_min());
            weatherEntity.setMaxTemp(openWeatherResponseVO.getMain().getTemp_max());
            weatherEntity.setPressure(openWeatherResponseVO.getMain().getPressure());
            weatherEntity.setHumidity(openWeatherResponseVO.getMain().getHumidity());
        }
        if (openWeatherResponseVO.getWind() != null) {
            weatherEntity.setWindSpeed(openWeatherResponseVO.getWind().getSpeed());
            weatherEntity.setWindDirection(openWeatherResponseVO.getWind().getDeg());
        }
        return weatherEntity;
    }

    private PincodeEntity createPincodeWithCoordinates(long pincode) {
        OpenWeatherCoordinatesResponseVO coordinatesResponseVO = openWeatherService.getCoordinatesForPincode(pincode);
        CoordinatesEntity coordinatesEntity = toCoordinatesEntity(coordinatesResponseVO);
        PincodeEntity pincodeEntity = new PincodeEntity();
        pincodeEntity.setPincode(pincode);
        pincodeEntity.setCoordinatesEntity(coordinatesEntity);
        return pincodeRepository.save(pincodeEntity);
    }

    private CoordinatesEntity toCoordinatesEntity(OpenWeatherCoordinatesResponseVO coordinatesResponseVO) {
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        coordinatesEntity.setLatitude(coordinatesResponseVO.getLat());
        coordinatesEntity.setLongitude(coordinatesResponseVO.getLon());
        return coordinatesEntity;
    }

    private LocalDate parseDate(String date) {
        LocalDate parsedDate = SupportedDatePatterns.parse(date);
        if (parsedDate == null) {
            throw new RuntimeException(String.format("Invalid date: %s. Supported Date Formats are %s", date,
                    SupportedDatePatterns.getSupportedDateRegex()));
        }
        return parsedDate;
    }
}
