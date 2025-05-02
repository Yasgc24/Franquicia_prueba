package com.prueba.franquicia.service;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.repository.FranquiciaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.bson.types.ObjectId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    private Franquicia franquicia;
    private Sucursal sucursal;

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

    @Test
    void testAgregarSucursal() {
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(new Franquicia("Franquicia 1")));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Franquicia> result = franquiciaService.agregarSucursal("1", new Sucursal("Sucursal 1"));

        assertNotNull(result);
        Franquicia savedFranquicia = result.block();
        assertFalse(savedFranquicia.getSucursales().isEmpty());
        assertNotNull(savedFranquicia.getSucursales().get(0).getId());
        assertEquals("Sucursal 1", savedFranquicia.getSucursales().get(0).getNombre());
    }

    @Test
    void testActualizarNombreSucursal() {
        ObjectId sucursalId = new ObjectId();
        Sucursal original = new Sucursal("Sucursal Original");
        original.setId(sucursalId);
        franquicia.agregarSucursal(original);

        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> {
            Franquicia f = invocation.getArgument(0);
            return Mono.just(f);
        });

        Mono<Franquicia> result = franquiciaService.actualizarNombreSucursal("1", sucursalId.toString(), "Sucursal Actualizada");

        StepVerifier.create(result)
            .assertNext(f -> {
                Sucursal actualizada = f.getSucursales().stream()
                    .filter(s -> s.getId().equals(sucursalId))
                    .findFirst()
                    .orElse(null);
                assertNotNull(actualizada);
                assertEquals("Sucursal Actualizada", actualizada.getNombre());
            })
            .verifyComplete();
    }

    @Test
    void testActualizarNombreSucursalNotFound() {
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));

        Mono<Franquicia> result = franquiciaService.actualizarNombreSucursal(
            "1", "1", "Sucursal principal");

        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}