package com.prueba.franquicia.repository;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@DataMongoTest
public class FranquiciaRepositoryTest {
    
    @Autowired
    private FranquiciaRepository franquiciaRepository;

    @BeforeEach
    void setUp() {
        franquiciaRepository.deleteAll().block();
    }

    @Test
    void testFindByNombre() {
        Producto producto = new Producto("Hamburguesa", 20);
        Sucursal sucursal = new Sucursal("Santa Monica", List.of(producto));
        Franquicia franquicia = new Franquicia(null, "Quesudos", List.of(sucursal));
        franquiciaRepository.save(franquicia).block();

        Mono<Franquicia> result = franquiciaRepository.findByNombre("Quesudos");

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getNombre().equals("Quesudos"))
                .verifyComplete();
    }

    @Test
    void testGuardarYBuscarFranquicia() {
        Producto producto = new Producto("Hamburguesa", 20);
        Sucursal sucursal = new Sucursal("Santa Monica", List.of(producto));
        Franquicia franquicia = new Franquicia(null, "Quesudos", List.of(sucursal));
        franquiciaRepository.save(franquicia).block();

        Mono<Franquicia> result = franquiciaRepository.findById(franquicia.getId());

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getNombre().equals("Quesudos"))
                .verifyComplete();

        
    }
}
