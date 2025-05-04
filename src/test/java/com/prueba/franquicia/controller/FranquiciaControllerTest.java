package com.prueba.franquicia.controller;

import com.prueba.franquicia.dto.NombreDTO;
import com.prueba.franquicia.dto.StockDTO;
import com.prueba.franquicia.dto.ProductoConSucursalDTO;
import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Producto;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.service.FranquiciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

class FranquiciaControllerTest {

    @Mock
    private FranquiciaService franquiciaService;

    @InjectMocks
    private FranquiciaController franquiciaController;

    private Franquicia franquicia;
    private Sucursal sucursal;
    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        franquicia = new Franquicia("Franquicia Test");
        sucursal = new Sucursal("Sucursal Test");
        producto = new Producto("Producto Test", 10);
        franquicia.agregarSucursal(sucursal);
        sucursal.agregarProducto(producto);
    }

    @Test
    void testAgregarFranquicia() {
        when(franquiciaService.agregarFranquicia(any(Franquicia.class)))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .agregarFranquicia(new Franquicia("Nueva Franquicia"));

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals("Franquicia Test", responseEntity.getBody().getNombre());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreFranquicia() {
        NombreDTO nombreDTO = new NombreDTO("Franquicia Actualizada");
        when(franquiciaService.actualizarNombreFranquicia(anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreFranquicia("1", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreFranquiciaNotFound() {
        NombreDTO nombreDTO = new NombreDTO("Franquicia Actualizada");
        when(franquiciaService.actualizarNombreFranquicia(anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Franquicia no encontrada")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreFranquicia("999", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testAgregarSucursal() {
        when(franquiciaService.agregarSucursal(anyString(), any(Sucursal.class)))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .agregarSucursal("1", new Sucursal("Nueva Sucursal"));

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals(1, responseEntity.getBody().getSucursales().size());
                })
                .verifyComplete();
    }

    @Test
    void testAgregarSucursalNotFound() {
        when(franquiciaService.agregarSucursal(anyString(), any(Sucursal.class)))
                .thenReturn(Mono.error(new RuntimeException("Franquicia no encontrada")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .agregarSucursal("999", new Sucursal("Nueva Sucursal"));

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreSucursal() {
        NombreDTO nombreDTO = new NombreDTO("Sucursal Actualizada");
        when(franquiciaService.actualizarNombreSucursal(anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreSucursal("1", "Sucursal Test", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreSucursalNotFound() {
        NombreDTO nombreDTO = new NombreDTO("Sucursal Actualizada");
        when(franquiciaService.actualizarNombreSucursal(anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Sucursal no encontrada")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreSucursal("1", "Sucursal Inexistente", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testAgregarProducto() {
        when(franquiciaService.agregarProducto(anyString(), anyString(), any(Producto.class)))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .agregarProducto("1", "Sucursal Test", new Producto("Nuevo Producto", 5));

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                    assertEquals(1, responseEntity.getBody().getSucursales().get(0).getProductos().size());
                })
                .verifyComplete();
    }

    @Test
    void testAgregarProductoNotFound() {
        when(franquiciaService.agregarProducto(anyString(), anyString(), any(Producto.class)))
                .thenReturn(Mono.error(new RuntimeException("Sucursal no encontrada")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .agregarProducto("1", "Sucursal Inexistente", new Producto("Nuevo Producto", 5));

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreProducto() {
        NombreDTO nombreDTO = new NombreDTO("Producto Actualizado");
        when(franquiciaService.actualizarNombreProducto(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreProducto("1", "Sucursal Test", "Producto Test", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testActualizarNombreProductoNotFound() {
        NombreDTO nombreDTO = new NombreDTO("Producto Actualizado");
        when(franquiciaService.actualizarNombreProducto(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Producto no encontrado")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .actualizarNombreProducto("1", "Sucursal Test", "Producto Inexistente", nombreDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testEliminarProducto() {
        when(franquiciaService.eliminarProducto(anyString(), anyString(), anyString()))
        .thenReturn(Mono.empty());
    
        Mono<ResponseEntity<Void>> resultado = franquiciaController
        .eliminarProducto("1", "Sucursal Test", "Producto Test");
    
        StepVerifier.create(resultado)
            .assertNext(responseEntity -> {
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            })
            .verifyComplete();
        }
        
    @Test
    void testEliminarProductoNotFound() {
        when(franquiciaService.eliminarProducto(anyString(), anyString(), anyString()))
        .thenReturn(Mono.error(new RuntimeException("Producto no encontrado")));
        
        Mono<ResponseEntity<Void>> resultado = franquiciaController
        .eliminarProducto("1", "Sucursal Test", "Producto Inexistente");
        
        StepVerifier.create(resultado)
        .assertNext(responseEntity -> {
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        })
        .verifyComplete();
    }

    @Test
    void testModificarStockProducto() {
        StockDTO stockDTO = new StockDTO(20);
        when(franquiciaService.modificarStock(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.just(franquicia));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .modificarStockProducto("1", "Sucursal Test", "Producto Test", stockDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void testModificarStockProductoNotFound() {
        StockDTO stockDTO = new StockDTO(20);
        when(franquiciaService.modificarStock(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Mono.error(new RuntimeException("Producto no encontrado")));

        Mono<ResponseEntity<Franquicia>> resultado = franquiciaController
                .modificarStockProducto("1", "Sucursal Test", "Producto Inexistente", stockDTO);

        StepVerifier.create(resultado)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    void productoConMasStockPorSucursal() {
        String franquiciaId = "1";
    
        Producto producto1 = new Producto("Producto X", 10);
        Producto producto2 = new Producto("Producto Y", 20);
        
        ProductoConSucursalDTO productoConSucursal1 = 
                new ProductoConSucursalDTO(producto1, "123", "Sucursal X");
        ProductoConSucursalDTO productoConSucursal2 = 
                new ProductoConSucursalDTO(producto2, "145", "Sucursal Y");
        
        List<ProductoConSucursalDTO> productosConStock = 
                Arrays.asList(productoConSucursal1, productoConSucursal2);

        when(franquiciaService.productoConMasStockPorSucursal(franquiciaId))
                .thenReturn(Flux.fromIterable(productosConStock));

        Flux<ProductoConSucursalDTO> response = 
                franquiciaController.productoConMasStockPorSucursal(franquiciaId);

        StepVerifier.create(response)
                .expectNext(productoConSucursal1)
                .expectNext(productoConSucursal2)
                .verifyComplete();

        verify(franquiciaService, times(1)).productoConMasStockPorSucursal(franquiciaId);
   }
}