package com.trimbleCars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

    private String make;
    private String model;
    private String variant;
    private String registrationNumber;
    private Long ownerId;
    
}
