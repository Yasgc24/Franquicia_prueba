package com.prueba.franquicia.service;

import com.prueba.franquicia.dto.AuthRequest;
import com.prueba.franquicia.dto.AuthResponse;
import com.prueba.franquicia.dto.RegisterRequest;
import com.prueba.franquicia.exception.BadRequestException;
import com.prueba.franquicia.model.Usuario;
import com.prueba.franquicia.repository.UsuarioRepository;
import com.prueba.franquicia.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Mono<AuthResponse> login(AuthRequest authRequest) {
        return userRepository.findByUsername(authRequest.getUsername())
                .filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> new AuthResponse(jwtUtil.generateToken(user), user.getUsername()))
                .switchIfEmpty(Mono.error(new BadRequestException("Credenciales inválidas")));
    }

    public Mono<Usuario> register(RegisterRequest registerRequest) {
        return userRepository.findByUsername(registerRequest.getUsername())
                .flatMap(existingUser -> Mono.<Usuario>error(new BadRequestException("Usuario ya existe")))
                .switchIfEmpty(Mono.defer(() -> {
                    Usuario newUser = new Usuario();
                    newUser.setUsername(registerRequest.getUsername());
                    newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                    newUser.setEnabled(true);
                    return userRepository.save(newUser);
                }));
    }
}