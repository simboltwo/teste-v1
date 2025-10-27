package com.naapi.naapi.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeiInsertDTO {

    @NotNull(message = "A data de início é obrigatória.")
    @PastOrPresent(message = "A data de início não pode ser no futuro.")
    private LocalDate dataInicio;

    @FutureOrPresent(message = "A data de fim não pode ser no passado.")
    private LocalDate dataFim;
    @NotBlank(message = "O campo 'metas' é obrigatório.")
    private String metas;

    @NotBlank(message = "O campo 'estratégias' é obrigatório.")
    private String estrategias;

    private String avaliacao;

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;

    @NotNull(message = "O ID do usuário responsável é obrigatório.")
    private Long responsavelId;
}