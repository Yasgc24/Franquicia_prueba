package com.prueba.franquicia.config;

import com.prueba.franquicia.model.Usuario;
import com.prueba.franquicia.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByUsername("admin")
                    .switchIfEmpty(userRepository.save(new Usuario(
                            null,
                            "admin",
                            passwordEncoder.encode("admin123"),
                            true
                    )))
                    .subscribe();
        };
    }
}