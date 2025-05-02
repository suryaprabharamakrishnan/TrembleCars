package com.trimbleCars.controller;

import com.trimbleCars.dto.*;
import com.trimbleCars.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@RequestBody UserRequest dto) {
        return  userService.registerUser(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return  userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'customer', 'car_owner')")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return  userService.getUserById(id);
    }

}
