package com.trimbleCars.service;

import com.trimbleCars.dto.CarRequest;
import com.trimbleCars.dto.CarResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.exception.CustomException;
import com.trimbleCars.model.Car;
import com.trimbleCars.model.CarStatus;
import com.trimbleCars.model.User;
import com.trimbleCars.repository.CarRepository;
import com.trimbleCars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    UserRepository userRepository;
 
    public ResponseEntity<SuccessResponse> addCar(CarRequest dto) {

        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new CustomException("Owner not found", HttpStatus.NOT_FOUND));

        Car car = new Car();
        car.setMake(dto.getMake());
        car.setModel(dto.getModel());
        car.setVariant(dto.getVariant());
        car.setRegistrationNumber(dto.getRegistrationNumber());
        car.setStatus(CarStatus.IDLE);
        car.setOwner(owner);

        carRepository.save(car);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Car Details Created Successfully");
        return ok().body(successResponse);

    }

    public ResponseEntity<SuccessResponse> updateCarStatus(Long carId, String status) {

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CustomException("Car not found",HttpStatus.NOT_FOUND));

        try {
            car.setStatus(CarStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new CustomException("Invalid status: " + status,HttpStatus.NOT_FOUND);
        }

        carRepository.save(car);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Car Details Updated Successfully");
        return ok().body(successResponse);
    }

    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }
 
    public ResponseEntity<List<CarResponse>> getCarsByOwner(Long ownerId) {

        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException("Owner not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(carRepository.findByOwner(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }
 
    public ResponseEntity<CarResponse> getCarById(Long id) {
        return ResponseEntity.ok(carRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new CustomException("Car not found",HttpStatus.NOT_FOUND)));
    }

    private CarResponse mapToResponse(Car car) {
        CarResponse res = new CarResponse();
        res.setId(car.getId());
        res.setMake(car.getMake());
        res.setModel(car.getModel());
        res.setVariant(car.getVariant());
        res.setRegistrationNumber(car.getRegistrationNumber());
        res.setStatus(car.getStatus().name());
        res.setOwnerName(car.getOwner().getName());
        return res;
    }
}
