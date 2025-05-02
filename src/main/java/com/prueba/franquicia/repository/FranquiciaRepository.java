package com.prueba.franquicia.repository;

import com.prueba.franquicia.model.Franquicia;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository extends ReactiveCrudRepository<Franquicia, String> {
    Mono<Franquicia> findByNombre(String nombre);
}
