package com.raxrot.back.controller;

import com.raxrot.back.dto.AuthResponse;
import com.raxrot.back.dto.UserRequest;
import com.raxrot.back.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest request) {
        AuthResponse authResponse=userService.register(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserRequest request) {
        AuthResponse authResponse=userService.login(request);
        return ResponseEntity.ok(authResponse);
    }
}
