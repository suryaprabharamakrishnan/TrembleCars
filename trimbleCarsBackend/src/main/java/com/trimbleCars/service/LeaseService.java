package com.trimbleCars.service;

import com.trimbleCars.dto.LeaseRequest;
import com.trimbleCars.dto.LeaseResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.exception.CustomException;
import com.trimbleCars.model.Car;
import com.trimbleCars.model.CarStatus;
import com.trimbleCars.model.Lease;
import com.trimbleCars.model.User;
import com.trimbleCars.repository.CarRepository;
import com.trimbleCars.repository.LeaseRepository;
import com.trimbleCars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class LeaseService {
    
    @Autowired
    LeaseRepository leaseRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    CarRepository carRepository;
     
    public ResponseEntity<SuccessResponse> startLease(LeaseRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CustomException("Car not found", HttpStatus.NOT_FOUND));

        if (car.getStatus() != CarStatus.IDLE) {
            throw new CustomException("Car not available for lease", HttpStatus.CONFLICT);
        }

        long activeLeases = leaseRepository.countByCustomerAndEndDateIsNull(user);

        if (activeLeases >= 2) {
            throw new CustomException("User already has 2 active leases", HttpStatus.BAD_REQUEST);
        }

        Lease lease = new Lease();
        lease.setCustomer(user);
        lease.setCar(car);
        lease.setStartDate(LocalDateTime.now());
        lease.setEndDate(null);

        car.setStatus(CarStatus.ON_LEASE);
        carRepository.save(car);
        leaseRepository.save(lease);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Lease Details Created Successfully");
        return ok().body(successResponse);
    }

     
    public ResponseEntity<SuccessResponse> endLease(Long leaseId) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new CustomException("Lease not found", HttpStatus.NOT_FOUND));

        if (lease.getEndDate() != null) {
            throw new CustomException("Lease already ended", HttpStatus.CONFLICT );
        }

        lease.setEndDate(LocalDateTime.now());
        leaseRepository.save(lease);

        Car car = lease.getCar();
        car.setStatus(CarStatus.IDLE);
        carRepository.save(car);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Lease Details Updated Successfully");
        return ok().body(successResponse);

    }

     
    public ResponseEntity<List<LeaseResponse>> getLeaseHistoryByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(leaseRepository.findByCustomer(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

     
    public ResponseEntity<List<LeaseResponse>> getAllLeases() {
        return ResponseEntity.ok(leaseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    private LeaseResponse mapToResponse(Lease lease) {
        LeaseResponse dto = new LeaseResponse();
        dto.setId(lease.getId());
        dto.setUserName(lease.getCustomer().getName());
        dto.setCarModel(lease.getCar().getModel());
        dto.setCarNumber(lease.getCar().getRegistrationNumber());
        dto.setLeaseStartTime(lease.getStartDate());
        dto.setLeaseEndTime(lease.getEndDate());
        dto.setStatus(lease.getStartDate() == null ? "ACTIVE" : "ENDED");
        return dto;
    }

    
}
