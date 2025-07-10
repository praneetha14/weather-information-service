package com.weather.info.repository;

import com.weather.info.entity.PincodeEntity;
import com.weather.info.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<WeatherEntity, UUID> {
    Optional<WeatherEntity> findByPincodeAndDate(PincodeEntity pincode, LocalDate date);
}
