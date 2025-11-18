package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtendimentoStatusUpdateDTO {

    @NotBlank(message = "O status é obrigatório.")
    private String status;
}