package com.trimbleCars.service;

import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.dto.UserRequest;
import com.trimbleCars.dto.UserResponse;
import com.trimbleCars.exception.CustomException;
import com.trimbleCars.model.Roles;
import com.trimbleCars.model.User;
import com.trimbleCars.repository.RolesRepository;
import com.trimbleCars.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository roleRepository;

    public ResponseEntity<SuccessResponse> registerUser(UserRequest dto) {
        Roles role = roleRepository.findByNameIgnoreCase (dto.getRole())
                .orElseThrow(() -> new CustomException("Invalid Role", HttpStatus.NOT_FOUND));

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);

        userRepository.save(user);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("User Registered Successfully");
        return ok().body(successResponse);
    }

    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<UserResponse> getUserById(Long id) {
        return ResponseEntity.ok(userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND)));
    }

    private UserResponse mapToResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getName());
        return dto;
    }

}
