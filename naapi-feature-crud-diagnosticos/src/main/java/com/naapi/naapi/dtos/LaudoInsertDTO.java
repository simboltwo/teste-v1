package com.naapi.naapi.dtos;

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
public class LaudoInsertDTO {

    @PastOrPresent(message = "A data de emissão não pode ser no futuro.")
    private LocalDate dataEmissao;

    // --- REMOVIDO ---
    // @NotBlank(message = "A URL do arquivo é obrigatória.")
    // private String urlArquivo;
    // --- FIM DA REMOÇÃO ---

    private String descricao;

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;
}