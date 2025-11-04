package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO "limpo" para o frontend enviar, sem ID de aluno ou ID próprio
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsavelInsertDTO {

    @NotBlank(message = "O nome do responsável é obrigatório.")
    private String nome;
    private String parentesco;
    private String telefone;
    private Boolean autorizadoBuscar;
}