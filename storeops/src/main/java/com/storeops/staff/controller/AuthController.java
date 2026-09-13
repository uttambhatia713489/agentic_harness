package com.storeops.staff.controller;

import com.storeops.staff.dto.LoginRequest;
import com.storeops.staff.dto.LoginResponse;
import com.storeops.staff.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StaffService staffService;

    public AuthController(final StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(staffService.login(request));
    }
}
