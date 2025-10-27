package com.naapi.naapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAlunosPorCursoDTO {
    
    private String cursoNome;
    private Long totalAlunos;
}