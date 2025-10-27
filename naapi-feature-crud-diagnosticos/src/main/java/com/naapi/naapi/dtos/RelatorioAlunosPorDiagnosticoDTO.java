package com.naapi.naapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAlunosPorDiagnosticoDTO {
    
    private String diagnosticoNome;
    private Long totalAlunos;
}