package com.prueba.franquicia.service;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.model.Sucursal;
import com.prueba.franquicia.dto.ProductoConSucursalDTO;
import com.prueba.franquicia.repository.FranquiciaRepository;
import com.prueba.franquicia.model.Producto;
import com.prueba.franquicia.exception.ResourceNotFoundException;
import com.prueba.franquicia.exception.BadRequestException;
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
        if (franquicia.getNombre() == null || franquicia.getNombre().trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nombre de la franquicia no puede estar vacío"));
        }
        return franquiciaRepository.save(franquicia);
    }

    // Metodo para actualizar el nombre de una franquicia
    public Mono<Franquicia> actualizarNombreFranquicia(String franquiciaId, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nuevo nombre no puede estar vacío"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                franquicia.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para agregar una sucursal a una franquicia
    public Mono<Franquicia> agregarSucursal(String franquiciaId, Sucursal sucursal) {
        if (sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nombre de la sucursal no puede estar vacío"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                sucursal.setId(new ObjectId());
                franquicia.getSucursales().add(sucursal);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para actualizar el nombre de una sucursal
    public Mono<Franquicia> actualizarNombreSucursal(String franquiciaId, String sucursalId, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nuevo nombre no puede estar vacío"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId;
                try {
                    objectIdSucursalId = new ObjectId(sucursalId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de sucursal inválido: " + sucursalId));
                }

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElse(null);
                
                if (sucursal == null) {
                    return Mono.error(new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
                }

                sucursal.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para agregar un producto a una sucursal de una franquicia
    public Mono<Franquicia> agregarProducto(String franquiciaId, String sucursalId, Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nombre del producto no puede estar vacío"));
        }
        
        if (producto.getStock() < 0) {
            return Mono.error(new BadRequestException("El stock no puede ser negativo"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId;
                try {
                    objectIdSucursalId = new ObjectId(sucursalId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de sucursal inválido: " + sucursalId));
                }

                Optional<Sucursal> sucursalOpt = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst();

                if (sucursalOpt.isEmpty()) {
                    return Mono.error(new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
                }

                Sucursal sucursal = sucursalOpt.get();
                sucursal.getProductos().add(producto);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para actualizar el nombre de un producto
    public Mono<Franquicia> actualizarNombreProducto(String franquiciaId, String sucursalId, String productoId, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new BadRequestException("El nuevo nombre no puede estar vacío"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId;
                ObjectId objectIdProductoId;
                
                try {
                    objectIdSucursalId = new ObjectId(sucursalId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de sucursal inválido: " + sucursalId));
                }
                
                try {
                    objectIdProductoId = new ObjectId(productoId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de producto inválido: " + productoId));
                }

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElse(null);
                
                if (sucursal == null) {
                    return Mono.error(new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
                }

                Producto producto = sucursal.getProductos().stream()
                    .filter(p -> p.getId().equals(objectIdProductoId))
                    .findFirst()
                    .orElse(null);
                
                if (producto == null) {
                    return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + productoId));
                }

                producto.setNombre(nuevoNombre);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Metodo para eliminar un producto
    public Mono<Void> eliminarProducto(String franquiciaId, String sucursalId, String productoId) {
        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
                .flatMap(franquicia -> {
                    ObjectId objectIdSucursalId;
                    ObjectId objectIdProductoId;
                    
                    try {
                        objectIdSucursalId = new ObjectId(sucursalId);
                    } catch (IllegalArgumentException e) {
                        return Mono.error(new BadRequestException("ID de sucursal inválido: " + sucursalId));
                    }
                    
                    try {
                        objectIdProductoId = new ObjectId(productoId);
                    } catch (IllegalArgumentException e) {
                        return Mono.error(new BadRequestException("ID de producto inválido: " + productoId));
                    }

                    Optional<Sucursal> sucursalOpt = franquicia.getSucursales().stream()
                            .filter(s -> s.getId().equals(objectIdSucursalId))
                            .findFirst();
    
                    if (sucursalOpt.isEmpty()) {
                        return Mono.error(new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
                    }
    
                    Sucursal sucursal = sucursalOpt.get();
                    
                    boolean eliminado = sucursal.getProductos().removeIf(p -> p.getId().equals(objectIdProductoId));
                    
                    if (!eliminado) {
                        return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + productoId));
                    }
                    
                    return franquiciaRepository.save(franquicia).then();
                });
    }

    // Metodo para modificar el stock de un producto en una sucursal
    public Mono<Franquicia> modificarStock(String franquiciaId, String sucursalId, String productoId, int stock) {
        if (stock < 0) {
            return Mono.error(new BadRequestException("El stock no puede ser negativo"));
        }
        
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMap(franquicia -> {
                ObjectId objectIdSucursalId;
                ObjectId objectIdProductoId;
                
                try {
                    objectIdSucursalId = new ObjectId(sucursalId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de sucursal inválido: " + sucursalId));
                }
                
                try {
                    objectIdProductoId = new ObjectId(productoId);
                } catch (IllegalArgumentException e) {
                    return Mono.error(new BadRequestException("ID de producto inválido: " + productoId));
                }

                Sucursal sucursal = franquicia.getSucursales().stream()
                    .filter(s -> s.getId().equals(objectIdSucursalId))
                    .findFirst()
                    .orElse(null);
                
                if (sucursal == null) {
                    return Mono.error(new ResourceNotFoundException("Sucursal no encontrada con ID: " + sucursalId));
                }

                Producto producto = sucursal.getProductos().stream()
                    .filter(p -> p.getId().equals(objectIdProductoId))
                    .findFirst()
                    .orElse(null);
                
                if (producto == null) {
                    return Mono.error(new ResourceNotFoundException("Producto no encontrado con ID: " + productoId));
                }

                producto.setStock(stock);
                return franquiciaRepository.save(franquicia);
            });
    }

    // Método para obtener el producto con más stock por cada sucursal en una franquicia
    public Flux<ProductoConSucursalDTO> productoConMasStockPorSucursal(String franquiciaId) {
        return franquiciaRepository.findById(franquiciaId)
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquiciaId)))
            .flatMapMany(franquicia -> {
                if (franquicia.getSucursales().isEmpty()) {
                    return Flux.empty();
                }
                
                return Flux.fromIterable(franquicia.getSucursales())
                    .flatMap(sucursal -> {
                        if (sucursal.getProductos().isEmpty()) {
                            return Flux.empty();
                        }
                        
                        return Flux.fromIterable(sucursal.getProductos())
                            .sort((p1, p2) -> Integer.compare(p2.getStock(), p1.getStock()))
                            .take(1)
                            .map(producto -> new ProductoConSucursalDTO(
                                producto, 
                                sucursal.getId().toString(), 
                                sucursal.getNombre())
                            );
                    });
            });
    }
}