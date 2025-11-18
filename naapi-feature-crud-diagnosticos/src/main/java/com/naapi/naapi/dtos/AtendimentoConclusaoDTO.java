/*
 * Arquivo NOVO: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/dtos/AtendimentoConclusaoDTO.java
 * Descrição: DTO específico para o modal de conclusão de atendimento.
 */
package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtendimentoConclusaoDTO {

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;
    
    @NotBlank(message = "O status é obrigatório.")
    private String status; // Ex: "REALIZADO" ou "CANCELADO"
}