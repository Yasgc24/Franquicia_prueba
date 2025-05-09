package com.prueba.franquicia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class FranquiciaApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("MONGODB_USER", dotenv.get("MONGODB_USER"));
        System.setProperty("MONGODB_PASSWORD", dotenv.get("MONGODB_PASSWORD"));
        System.setProperty("MONGODB_CLUSTER", dotenv.get("MONGODB_CLUSTER"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
        
        SpringApplication.run(FranquiciaApplication.class, args);
    }
}
