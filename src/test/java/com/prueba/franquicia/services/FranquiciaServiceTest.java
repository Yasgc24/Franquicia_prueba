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
    void testAgregarSucursal() {
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(new Franquicia("Franquicia 1"))); // Devuelve una NUEVA instancia
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0))); // Simplemente devuelve el objeto guardado

        Mono<Franquicia> result = franquiciaService.agregarSucursal("1", new Sucursal("Sucursal 1"));

        assertNotNull(result);
        Franquicia savedFranquicia = result.block();
        assertFalse(savedFranquicia.getSucursales().isEmpty());
        assertNotNull(savedFranquicia.getSucursales().get(0).getId()); // Verifica que el ID no sea nulo
        assertEquals("Sucursal 1", savedFranquicia.getSucursales().get(0).getNombre()); // Verifica el nombre
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

    @Test
    void testAgregarProducto() {
        ObjectId sucursalId = new ObjectId();
        Sucursal centro = new Sucursal("Centro");
        centro.setId(sucursalId);
        franquicia.getSucursales().add(centro);
        Producto nuevo = new Producto("Hamburguesa", 20);

        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> {
            Franquicia f = invocation.getArgument(0);
            return Mono.just(f);
        });

        StepVerifier.create(franquiciaService.agregarProducto("1", sucursalId.toString(), nuevo))
            .expectNextMatches(f -> f.getSucursales().get(0).getProductos().get(0).getNombre().equals("Hamburguesa"))
            .verifyComplete();
    }

    @Test
    void testActualizarNombreProducto() {
        ObjectId sucursalId = new ObjectId();
        ObjectId productoId = new ObjectId();
        Producto originalProducto = new Producto();
        originalProducto.setId(productoId);
        originalProducto.setNombre("Producto Original");
        originalProducto.setStock(10);
        Sucursal testSucursal = new Sucursal("Sucursal Test", new ArrayList<>(List.of(originalProducto)));
        testSucursal.setId(sucursalId);
        franquicia.agregarSucursal(testSucursal);
    
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> {
            Franquicia f = invocation.getArgument(0);
            return Mono.just(f);
        });
    
        Mono<Franquicia> result = franquiciaService.actualizarNombreProducto(
            "1", sucursalId.toString(), productoId.toString(), "Producto Actualizado");
    
        StepVerifier.create(result)
            .assertNext(f -> {
                Sucursal s = f.getSucursales().stream()
                    .filter(suc -> suc.getId().equals(sucursalId))
                    .findFirst()
                    .orElse(null);
    
                assertNotNull(s);
    
                Producto p = s.getProductos().stream()
                    .filter(prod -> prod.getId().equals(productoId))
                    .findFirst()
                    .orElse(null);
    
                assertNotNull(p);
                assertEquals("Producto Actualizado", p.getNombre());
                assertEquals(10, p.getStock());
            })
            .verifyComplete();
    }
    
    @Test
    void testActualizarNombreProductoNotFound() {
        ObjectId sucursalId = new ObjectId();
        ObjectId productoId = new ObjectId();
        Sucursal testSucursal = new Sucursal("Sucursal Test");
        testSucursal.setId(sucursalId);
        franquicia.agregarSucursal(testSucursal);
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
    
        Mono<Franquicia> result = franquiciaService.actualizarNombreProducto(
            "1", sucursalId.toString(), productoId.toString(), "Producto Actualizado");
    
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void testModificarStock() {
        ObjectId sucursalId = new ObjectId();
        ObjectId productoId = new ObjectId();
        Producto producto = new Producto();
        producto.setId(productoId);
        producto.setNombre("Hamburguesa");
        producto.setStock(20);
        Sucursal belen = new Sucursal("Belen", new ArrayList<>(List.of(producto)));
        belen.setId(sucursalId);
        franquicia.getSucursales().add(belen);
    
        when(franquiciaRepository.findById("1")).thenReturn(Mono.just(franquicia));
        when(franquiciaRepository.save(any(Franquicia.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
    
        StepVerifier.create(franquiciaService.modificarStock("1", sucursalId.toString(), productoId.toString(), 50))
            .expectNextMatches(f -> f.getSucursales().get(0).getProductos().get(0).getStock() == 50)
            .verifyComplete();
    }

    @Test
    void testProductoConMasStockPorSucursal() {
        ObjectId franquiciaId = new ObjectId();

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
}
