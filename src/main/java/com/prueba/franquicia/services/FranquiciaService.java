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
}