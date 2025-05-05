package com.prueba.franquicia.service;

import com.prueba.franquicia.exception.ResourceNotFoundException;
import com.prueba.franquicia.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService implements ReactiveUserDetailsService {

    private final UsuarioRepository userRepository;

    public UsuarioService(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado con el nombre de usuario: " + username)))
                .cast(UserDetails.class);
    }
}