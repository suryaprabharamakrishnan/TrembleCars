package com.trimbleCars.controller;

import com.trimbleCars.dto.RoleResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.dto.UpdateRoleRequest;
import com.trimbleCars.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    AuthService authService;

    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<SuccessResponse> updateRole(UpdateRoleRequest updateRoleRequest) {
        return authService.updateRole(updateRoleRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id) {
        return authService.getRoleById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<RoleResponse>> getAllRole() {
        return authService.getAllRole();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<SuccessResponse> deleteRoleDetails(Long id) {
        return authService.deleteRole(id);
    }
}
