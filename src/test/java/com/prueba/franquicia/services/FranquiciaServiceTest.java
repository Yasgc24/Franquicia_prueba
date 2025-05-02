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
        sucursal = new Sucursal("Sucursal 1");
        producto = new Producto("Producto 1", 50);
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

        String nuevoNombre = "Perritos 1";
        Mono<Franquicia> result = franquiciaService.actualizarNombreFranquicia("1", nuevoNombre);

        StepVerifier.create(result)
            .expectNextMatches(f -> nuevoNombre.equals(f.getNombre()))
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
    void testProductoConMasStockPorSucursal() {
        ObjectId franquiciaId = new ObjectId();
        ObjectId sucursalId = new ObjectId();
        
        Producto productoMayorStock = new Producto("Producto x", 100);
        Producto productoMenorStock = new Producto("Producto y", 50);
        
        Sucursal sucursal = new Sucursal("Sucursal principal");
        sucursal.setId(sucursalId);
        sucursal.agregarProducto(productoMayorStock);
        sucursal.agregarProducto(productoMenorStock);
        
        Franquicia franquicia = new Franquicia("Franquicia 1");
        franquicia.setId(franquiciaId);
        franquicia.agregarSucursal(sucursal);
        
        when(franquiciaRepository.findById(franquiciaId.toString()))
            .thenReturn(Mono.just(franquicia));
        
        Flux<ProductoConSucursal> result = franquiciaService.productoConMasStockPorSucursal(franquiciaId.toString());
        
        StepVerifier.create(result)
            .expectNextMatches(productoConSucursal -> {
                return productoConSucursal.getProducto().getNombre().equals("Producto x") &&
                    productoConSucursal.getProducto().getStock() == 100 &&
                    productoConSucursal.getSucursalId().equals(sucursalId.toString()) &&
                    productoConSucursal.getSucursalNombre().equals("Sucursal principal");
            })
            .expectComplete()
            .verify();
    }
}