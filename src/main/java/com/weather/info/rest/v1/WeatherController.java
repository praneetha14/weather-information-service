package com.weather.info.rest.v1;

import com.weather.info.model.WeatherResponseVO;
import com.weather.info.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WeatherController has Rest Endpoint that is used to fetch the weather information.
 */
@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/get")
    public ResponseEntity<WeatherResponseVO> getWeather(@RequestParam String date, @RequestParam long pincode) {
        return ResponseEntity.ok(weatherService.getWeatherInformation(pincode, date));
    }

}
