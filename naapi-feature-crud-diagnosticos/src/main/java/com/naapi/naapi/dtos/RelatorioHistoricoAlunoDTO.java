package com.naapi.naapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioHistoricoAlunoDTO {

    private AlunoDTO aluno;

    private List<LaudoDTO> laudos;

    private List<PeiDTO> peis;
    
    private List<HistoricoAcademicoDTO> historicoAcademico;
}