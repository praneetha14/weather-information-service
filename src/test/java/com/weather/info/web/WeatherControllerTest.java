package com.weather.info.web;

import com.weather.info.BaseControllerTest;
import com.weather.info.exception.ResourceNotFoundException;
import com.weather.info.model.WeatherResponseVO;
import com.weather.info.rest.v1.WeatherController;
import com.weather.info.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeatherController.class)
@ExtendWith(SpringExtension.class)
public class WeatherControllerTest extends BaseControllerTest {

    private static final String WEATHER_URL = "/api/v1/weather/get";

    private static final String DATE_PARAM = "date";
    private static final String PINCODE_PARAM = "pincode";

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getWeatherInformationSuccessTest() throws Exception {
        when(weatherService.getWeatherInformation(anyLong(), anyString()))
                .thenReturn(createWeatherResponseVO());
        String date = LocalDate.now().toString();
        mockMvc.perform(
                get(WEATHER_URL)
                        .param(DATE_PARAM, date)
                        .param(PINCODE_PARAM, "122017")
        ).andExpect(status().isOk());
    }

    @Test
    void getWeatherInformationWithBadRequestFailureTest() throws Exception {
        when(weatherService.getWeatherInformation(anyLong(), anyString()))
                .thenThrow(new RuntimeException());
        String date = LocalDate.now().toString();
        mockMvc.perform(
                get(WEATHER_URL)
                        .param(DATE_PARAM, date)
                        .param(PINCODE_PARAM, "122000")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getWeatherInformationWithNotFoundFailureTest() throws Exception {
        when(weatherService.getWeatherInformation(anyLong(), anyString()))
                .thenThrow(new ResourceNotFoundException("resource not found"));
        String date = LocalDate.now().toString();
        mockMvc.perform(
                get(WEATHER_URL)
                        .param(DATE_PARAM, date)
                        .param(PINCODE_PARAM, "122001")
        ).andExpect(status().isNotFound());
    }

    @Test
    void getWeatherInformationWithoutDateParamFailureTest() throws Exception {
        mockMvc.perform(
                get(WEATHER_URL)
                        .param(PINCODE_PARAM, "122017")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getWeatherInformationWithoutPincodeParamFailureTest() throws Exception {
        mockMvc.perform(
                get(WEATHER_URL)
                        .param(DATE_PARAM, LocalDate.now().toString())
        ).andExpect(status().isBadRequest());
    }


    private WeatherResponseVO createWeatherResponseVO() {
        return new WeatherResponseVO(LocalDate.now(), 122000, 273.15, "rainy",
                "heavy rain", 273.15, 273.15, 273.15,
                273.15, 83, 76, 345);
    }
}
