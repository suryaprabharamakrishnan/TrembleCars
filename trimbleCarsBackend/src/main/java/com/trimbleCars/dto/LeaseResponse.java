package com.trimbleCars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaseResponse {

    private Long id;
    private String userName;
    private String carModel;
    private String carNumber;
    private LocalDateTime leaseStartTime;
    private LocalDateTime leaseEndTime;
    private String status;
    
}
