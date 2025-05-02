package com.trimbleCars.controller;

import com.trimbleCars.dto.LeaseRequest;
import com.trimbleCars.dto.LeaseResponse;
import com.trimbleCars.dto.SuccessResponse;
import com.trimbleCars.service.LeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leases")
public class LeaseController {

    @Autowired
    LeaseService leaseService;

    @PostMapping("/start")
    @PreAuthorize("hasAnyAuthority('customer')")
    public ResponseEntity<SuccessResponse> startLease(@RequestBody LeaseRequest dto) {
        return leaseService.startLease(dto);
    }

    @PutMapping("/{leaseId}/end")
    @PreAuthorize("hasAnyAuthority('customer')")
    public ResponseEntity<SuccessResponse> endLease(@PathVariable Long leaseId) {
        return leaseService.endLease(leaseId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('customer', 'admin')")
    public ResponseEntity<List<LeaseResponse>> getUserHistory(@PathVariable Long userId) {
        return leaseService.getLeaseHistoryByUser(userId);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<LeaseResponse>> getAllLeases() {
        return leaseService.getAllLeases();
    }

}
