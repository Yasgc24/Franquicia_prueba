package com.prueba.franquicia.repository;

import com.prueba.franquicia.model.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, String> {
    Mono<Usuario> findByUsername(String user);
}