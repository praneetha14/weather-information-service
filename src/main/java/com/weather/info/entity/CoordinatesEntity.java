package com.weather.info.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "coordinates")
@Getter
@Setter
public class CoordinatesEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;
}