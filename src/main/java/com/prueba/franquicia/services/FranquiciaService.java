package com.prueba.franquicia.service;

import com.prueba.franquicia.model.Franquicia;
import com.prueba.franquicia.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
                sucursal.setId(new ObjectId()); // Generar ObjectId para la sucursal
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
}