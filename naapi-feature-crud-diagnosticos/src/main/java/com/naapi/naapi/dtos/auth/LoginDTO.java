package com.naapi.naapi.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}