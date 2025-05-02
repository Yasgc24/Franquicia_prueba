package com.prueba.franquicia.service;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.model.Producto;
import com.prueba.franquicia.repository.FranquiciaRepository;
import com.prueba.franquicia.service.FranquiciaService.ProductoConSucursal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.core.publisher.Flux;
import org.bson.types.ObjectId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    private Franquicia franquicia;
    private Sucursal sucursal;
    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        franquicia = new Franquicia("Perritos");
    }

    @Test
    void testAgregarFranquicia() {
        when(franquiciaRepository.save(franquicia)).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaService.agregarFranquicia(franquicia);

        assertNotNull(result);
        assertEquals("Perritos", result.block().getNombre());
    }

    @Test
    void testActualizarNombreFranquicia() {
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> {
            Franquicia f = invocation.getArgument(0);
            return Mono.just(f);
        });

        Mono<Franquicia> result = franquiciaService.actualizarNombreFranquicia("1", "Perritos 1");

        StepVerifier.create(result)
            .expectNextMatches(f -> "Franquicia Actualizada".equals(f.getNombre()))
            .verifyComplete();
    }

    @Test
    void testActualizarNombreFranquiciaNotFound() {
        when(franquiciaRepository.findById("999")).thenReturn(Mono.empty());

        Mono<Franquicia> result = franquiciaService.actualizarNombreFranquicia("999", "Perritos 1");

        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}