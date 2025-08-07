package com.manager.task.controller;

import com.manager.task.dtos.AuthResponse;
import com.manager.task.dtos.LoginRequest;
import com.manager.task.dtos.RegisterRequest;
import com.manager.task.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.register(request ,response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request,HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request ,response));
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return new AuthResponse(newAccessToken);
    }
}
