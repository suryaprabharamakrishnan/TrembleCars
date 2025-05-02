package com.trimbleCars.controller;

import com.trimbleCars.dto.CarRequest;
import com.trimbleCars.dto.CarResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('car_owner')")
    public ResponseEntity<SuccessResponse> addCar(@RequestBody CarRequest dto) {
        return carService.addCar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('customer', 'car_owner', 'admin')")
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyAuthority('car_owner', 'admin')")
    public ResponseEntity<List<CarResponse>> getByOwner(@PathVariable Long ownerId) {
        return carService.getCarsByOwner(ownerId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'customer', 'car_owner')")
    public ResponseEntity<CarResponse> getCar(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PutMapping("/{carId}/status")
    @PreAuthorize("hasAnyAuthority('car_owner', 'admin')")
    public ResponseEntity<SuccessResponse> updateStatus(@PathVariable Long carId, @RequestParam String status) {
        return carService.updateCarStatus(carId, status);
    }
}
