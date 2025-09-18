package com.weather.info.redis.service.Impl;

import com.weather.info.model.WeatherResponseVO;
import com.weather.info.redis.service.WeatherCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class WeatherCacheServiceImpl implements WeatherCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveWeatherInfo(String key, WeatherResponseVO weatherResponseVO, long ttl, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, weatherResponseVO, ttl, unit);
        log.info("Saved weather info to Redis with key: {}", key);
    }

    @Override
    public WeatherResponseVO getWeatherInfo(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        WeatherResponseVO info = (WeatherResponseVO) ops.get(key);

        if (info != null) {
            log.info("Cache HIT for key: {}", key);
        } else {
            log.info("Cache MISS for key: {}", key);
        }
        return info;
    }
}