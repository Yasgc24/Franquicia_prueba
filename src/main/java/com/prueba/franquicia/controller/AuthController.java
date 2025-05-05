package com.prueba.franquicia.controller;

import com.prueba.franquicia.dto.AuthRequest;
import com.prueba.franquicia.dto.AuthResponse;
import com.prueba.franquicia.dto.RegisterRequest;
import com.prueba.franquicia.model.Usuario;
import com.prueba.franquicia.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Usuario>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }
}