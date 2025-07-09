package com.weather.info.repository;

import com.weather.info.entity.PincodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PincodeRepository extends JpaRepository<PincodeEntity, UUID> {
    Optional<PincodeEntity> findByPincode(long pincode);
}
