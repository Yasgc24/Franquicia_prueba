package com.prueba.franquicia.controller;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Producto;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.service.FranquiciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    // Endpoint para agregar una nueva franquicia
    @PostMapping
    public Mono<ResponseEntity<Franquicia>> agregarFranquicia(@RequestBody Franquicia franquicia) {
        return franquiciaService.agregarFranquicia(franquicia)
                .map(f -> ResponseEntity.status(HttpStatus.CREATED).body(f));
    }

    // Endpoint para actualizar el nombre de una franquicia
    @PutMapping("/{franquiciaId}/nombre")
    public Mono<ResponseEntity<Franquicia>> actualizarNombreFranquicia(
            @PathVariable String franquiciaId,
            @RequestBody String nuevoNombre) {
        return franquiciaService.actualizarNombreFranquicia(franquiciaId, nuevoNombre)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Endpoint para agregar una nueva sucursal a una franquicia
    @PostMapping("/{franquiciaId}/sucursales")
    public Mono<ResponseEntity<Franquicia>> agregarSucursal(
            @PathVariable String franquiciaId,
            @RequestBody Sucursal sucursal) {
        return franquiciaService.agregarSucursal(franquiciaId, sucursal)
                .map(f -> ResponseEntity.status(HttpStatus.CREATED).body(f))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // Endpoint para actualizar el nombre de una sucursal
    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/nombre")
    public Mono<ResponseEntity<Franquicia>> actualizarNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @RequestBody String nuevoNombre) {
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalId, nuevoNombre)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Endpoint para agregar un nuevo producto a una sucursal
    @PostMapping("/{franquiciaId}/sucursales/{sucursalId}/productos")
    public Mono<ResponseEntity<Franquicia>> agregarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @RequestBody Producto producto) {
        return franquiciaService.agregarProducto(franquiciaId, sucursalId, producto)
                .map(f -> ResponseEntity.status(HttpStatus.CREATED).body(f))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    // Endpoint para actualizar el nombre de un producto
    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/nombre")
    public Mono<ResponseEntity<Franquicia>> actualizarNombreProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @RequestBody String nuevoNombre) {
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalId, productoId, nuevoNombre)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public Mono<ResponseEntity<Void>> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        return franquiciaService.eliminarProducto(franquiciaId, sucursalId, productoId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Endpoint para modificar el stock de un producto
    @PutMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public Mono<ResponseEntity<Franquicia>> modificarStockProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @RequestBody Integer stock) {
        return franquiciaService.modificarStock(franquiciaId, sucursalId, productoId, stock)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    // Endpoint para obtener el producto con más stock por sucursal para una franquicia
    @GetMapping("/{franquiciaId}/productos/mas-stock")
    public Flux<FranquiciaService.ProductoConSucursal> productoConMasStockPorSucursal(@PathVariable String franquiciaId) {
        return franquiciaService.productoConMasStockPorSucursal(franquiciaId);
    }

}