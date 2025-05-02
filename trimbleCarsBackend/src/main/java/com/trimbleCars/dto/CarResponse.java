package com.trimbleCars.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarResponse {

    private Long id;
    private String make;
    private String model;
    private String variant;
    private String registrationNumber;
    private String status;
    private String ownerName;

}
