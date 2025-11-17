package com.naapi.naapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO para atualizar apenas nome e email
@Data
public class UsuarioSelfUpdateDTO {
    
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank(message = "O email é obrigatório.")
    @Email
    private String email;
}