package com.weather.info;

import com.weather.info.client.OpenWeatherClient;
import com.weather.info.properties.ApplicationProperties;
import com.weather.info.properties.OpenWeatherConfigProperties;
import com.weather.info.redis.service.Impl.WeatherCacheServiceImpl;
import com.weather.info.repository.PincodeRepository;
import com.weather.info.repository.WeatherRepository;
import com.weather.info.service.OpenWeatherService;
import com.weather.info.service.WeatherService;
import com.weather.info.service.impl.OpenWeatherServiceImpl;
import com.weather.info.service.impl.WeatherServiceImpl;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties({OpenWeatherConfigProperties.class, ApplicationProperties.class})
public class WeatherInfoAutoConfiguration {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenWeatherClient openWeatherClient(RestTemplate restTemplate,
                                               OpenWeatherConfigProperties openWeatherConfigProperties) {
        return new OpenWeatherClient(restTemplate, openWeatherConfigProperties);
    }

    @Bean
    public OpenWeatherService openWeatherService(OpenWeatherClient openWeatherClient) {
        return new OpenWeatherServiceImpl(openWeatherClient);
    }

    @Bean
    public WeatherService weatherService(WeatherRepository weatherRepository, PincodeRepository pincodeRepository,
                                         OpenWeatherService openWeatherService, WeatherCacheServiceImpl weatherCacheServiceImpl) {
        return new WeatherServiceImpl(weatherRepository, pincodeRepository, openWeatherService, weatherCacheServiceImpl);
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.servers(List.of(new Server().url(applicationProperties.getBaseUrl())));
        return openAPI;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public WeatherCacheServiceImpl weatherCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        return new WeatherCacheServiceImpl(redisTemplate);
    }
}
