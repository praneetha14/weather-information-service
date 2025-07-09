package com.weather.info.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "pincode")
@Getter
@Setter
public class PincodeEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "pincode")
    private long pincode;

    @Column(name = "place")
    private String place;

    @Column(name = "country")
    private String country;

    @OneToOne(cascade = CascadeType.ALL)
    private CoordinatesEntity coordinatesEntity;
}
