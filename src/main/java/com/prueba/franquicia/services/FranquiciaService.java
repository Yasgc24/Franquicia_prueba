package com.prueba.franquicia.service;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.repository.FranquiciaRepository;
import com.prueba.franquicia.model.Producto;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    // Metodo para agregar una franquicia
    public Mono<Franquicia> agregarFranquicia(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    // Metodo para actualizar el nombre de una franquicia
    public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
            .flatMap(franquicia -> {
                franquicia.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para agregar una sucursal a una franquicia
    public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal) {
        return franquiciaRepository.findById(franquiciaId)
            .flatMap(franquicia -> {
                sucursal.setId(new ObjectId());
                franquicia.getSucursales().add(sucursal);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para actualizar el nombre de una sucursal
    public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId = new ObjectId(sucursalId);

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                sucursal.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para agregar un producto a una sucursal de una franquicia
    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto) {
        return franquiciaRepository.findById(franquiciaId)
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId = new ObjectId(sucursalId);

                Optional<Sucursal> sucursalOpt = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst();

                if (sucursalOpt.isEmpty()) {
                    return Mono.error(new RuntimeException("Sucursal no encontrada"));
                }

                Sucursal sucursal = sucursalOpt.get();
                sucursal.getProductos().add(producto);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para actualizar el nombre de un producto
    public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId = new ObjectId(sucursalId);

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                ObjectId objectIdProductoId = new ObjectId(productoId);
                Producto producto = sucursal.getProductos().stream()
                    .filter(p -> p.getId().equals(objectIdProductoId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                producto.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para eliminar un producto
    public Mono<Void> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return franquiciaRepository.findById(franquiciaId)
                .flatMap(franquicia -> {
                    ObjectId objectIdSucursalId = new ObjectId(sucursalId);
                    ObjectId objectIdProductoId = new ObjectId(productoId);
    
                    Optional<Sucursal> sucursalOpt = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(objectIdSucursalId))
                            .findFirst();
    
                    if (sucursalOpt.isEmpty()) {
                        return Mono.error(new RuntimeException("Sucursal no encontrada"));
                    }
    
                    Sucursal sucursal = sucursalOpt.get();
                    sucursal.getProductos().removeIf(p -> p.getId().equals(objectIdProductoId));
                    return franquiciaRepository.save(franquicia).then();
                });
    }

    // Metodo para modificar el stock de un producto en una sucursal
    public Mono<Franquicia> modificarStock(String franquiciaId, String sucursalId, String productoId, int stock) {
        return franquiciaRepository.findById(franquiciaId)
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId = new ObjectId(sucursalId);

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                ObjectId objectIdProductoId = new ObjectId(productoId);
                Producto producto = sucursal.getProductos().stream()
                    .filter(p -> p.getId().equals(objectIdProductoId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                producto.setStock(stock);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Método para obtener el producto con más stock por cada sucursal en una franquicia
    public Flux<ProductoConSucursal> productoConMasStockPorSucursal(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
            .flatMapMany(franquicia -> {
                return Flux.fromIterable(franquicia.getSucursales())
                    .flatMap(sucursal -> {
                        return Flux.fromIterable(sucursal.getProductos())
                            .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                            .take(1)
                            .map(producto -> new ProductoConSucursal(
                                producto, 
                                sucursal.getId().toString(), 
                                sucursal.getNombre())
                            );
                    });
            });
    }

    // Clase auxiliar para asociar un producto con la información de la sucursal a la que pertenece
    public static class ProductoConSucursal {
        private Producto producto;
        private String sucursalId;
        private String sucursalNombre;

        public ProductoConSucursal(Producto producto, String sucursalId, String sucursalNombre) {
            this.producto = producto;
            this.sucursalId = sucursalId;
            this.sucursalNombre = sucursalNombre;
        }

        public Producto getProducto() {
            return producto;
        }

        public void setProducto(Producto producto) {
            this.producto = producto;
        }

        public String getSucursalId() {
            return sucursalId;
        }

        public void setSucursalId(String sucursalId) {
            this.sucursalId = sucursalId;
        }

        public String getSucursalNombre() {
            return sucursalNombre;
        }

        public void setSucursalNombre(String sucursalNombre) {
            this.sucursalNombre = sucursalNombre;
        }
    }
}