package com.weather.info.client;

import com.weather.info.entity.CoordinatesEntity;
import com.weather.info.model.open.weather.OpenWeatherCoordinatesResponseVO;
import com.weather.info.model.open.weather.OpenWeatherResponseVO;
import com.weather.info.properties.OpenWeatherConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.weather.info.model.open.weather.utils.OpenWeatherEndpoints.GEO_CODING_URL;
import static com.weather.info.model.open.weather.utils.OpenWeatherEndpoints.WEATHER_URL;

@RequiredArgsConstructor
@Slf4j
public class OpenWeatherClient {

    private static final String DEFAULT_COUNTRY_CODE = "IN";

    private final RestTemplate restTemplate;

    private final OpenWeatherConfigProperties openWeatherConfigProperties;

    public OpenWeatherResponseVO getWeatherForCoordinates(CoordinatesEntity coordinatesEntity) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WEATHER_URL)
                .queryParam("lat", coordinatesEntity.getLatitude())
                .queryParam("lon", coordinatesEntity.getLongitude())
                .queryParam("appid", openWeatherConfigProperties.getAppId());
        log.info("Calling OpenWeather API to fetch weather info with url: {}", WEATHER_URL);
        try {
            return restTemplate.getForObject(builder.toUriString(), OpenWeatherResponseVO.class);
        } catch (Exception e) {
            log.error("Error occurred while fetching weather info.", e);
            throw new RuntimeException("Error occurred while fetching weather info.", e);
        }
    }

    public OpenWeatherCoordinatesResponseVO fetchCoordinatesForZipCode(String zipCode) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GEO_CODING_URL)
                .queryParam("zip", zipCode + "," + DEFAULT_COUNTRY_CODE)
                .queryParam("appid", openWeatherConfigProperties.getAppId());
        log.info("Calling OpenWeather API to fetch Coordinates with url: {}", GEO_CODING_URL);
        try {
            return restTemplate.getForObject(builder.toUriString(), OpenWeatherCoordinatesResponseVO.class);
        } catch (Exception e) {
            log.error("Error occurred while fetching Coordinates.", e);
            throw new RuntimeException("Error occurred while fetching Coordinates.Recheck your pincode and try again", e);
        }

    }
}
