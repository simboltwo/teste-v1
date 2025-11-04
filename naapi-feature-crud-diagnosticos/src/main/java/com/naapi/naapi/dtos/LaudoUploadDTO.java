package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaudoUploadDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "A data de emissão não pode ser no futuro.")
    private LocalDate dataEmissao;

    private String descricao;

    @NotNull(message = "O ID do aluno é obrigatório.")
    private Long alunoId;
}