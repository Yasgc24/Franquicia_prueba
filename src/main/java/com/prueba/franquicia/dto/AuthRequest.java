package com.prueba.franquicia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    
    @NotBlank(message = "Usuario no puede estar vacío")
    private String username;
    
    @NotBlank(message = "Contraseña no puede estar vacía")
    private String password;
}