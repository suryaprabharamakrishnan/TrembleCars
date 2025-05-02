package com.trimbleCars.service;

import com.trimbleCars.dto.RoleResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.dto.UpdateRoleRequest;
import com.trimbleCars.exception.CustomException;
import com.trimbleCars.model.Roles;
import com.trimbleCars.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    RolesRepository rolesRepository;

    public ResponseEntity<SuccessResponse> updateRole(UpdateRoleRequest updateRoleRequest) {

        Roles roles = rolesRepository.findById(updateRoleRequest.getId()). orElseThrow(() -> new CustomException("Role already exists", HttpStatus.CONFLICT));

        roles.setName(updateRoleRequest.getName());

        rolesRepository.save(roles);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Roles updated Successfully");
        return ResponseEntity.ok().body(successResponse);

    }

    public ResponseEntity<List<RoleResponse>> getAllRole() {

        List<Roles> roles = rolesRepository.findAll();

        List<RoleResponse> responses = roles.stream().map(role -> {
            RoleResponse response = new RoleResponse();
            response.setId(role.getId());
            response.setName(role.getName());
            return response;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<RoleResponse> getRoleById(Long id) {

        Roles existingRole = rolesRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role is not Found", HttpStatus.NOT_FOUND));
        rolesRepository.delete(existingRole);

        RoleResponse response = new RoleResponse();

        response.setId(existingRole.getId());
        response.setName(existingRole.getName());

        return  ResponseEntity.ok().body(response);
    }

    public ResponseEntity<SuccessResponse> deleteRole(Long id) {

        Roles existingRole = rolesRepository.findById(id)
                .orElseThrow(() -> new CustomException("Role is not Found", HttpStatus.NOT_FOUND));
        rolesRepository.delete(existingRole);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setMessage("Roles deleted successfully");
        return  ResponseEntity.ok().body(successResponse);

    }
}
