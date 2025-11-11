package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtendimentoInsertDTO {

    @NotNull(message = "A data e hora são obrigatórias.")
    private LocalDateTime dataHora;

    private String descricao;

    @NotBlank(message = "O status é obrigatório.")
    private String status;

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;

    @NotNull(message = "O ID do usuário responsável é obrigatório.")
    private Long responsavelId;

    @NotNull(message = "O ID do tipo de atendimento é obrigatório.")
    private Long tipoAtendimentoId;
}