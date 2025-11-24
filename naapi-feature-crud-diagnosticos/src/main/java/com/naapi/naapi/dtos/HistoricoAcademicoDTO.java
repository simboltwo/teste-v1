package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.HistoricoAcademico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoAcademicoDTO {

    private Long id;
    private Long alunoId;
    private CursoDTO curso;
    private TurmaDTO turma;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public HistoricoAcademicoDTO(HistoricoAcademico entity) {
        this.id = entity.getId();
        this.alunoId = entity.getAluno().getId();
        this.curso = new CursoDTO(entity.getCurso());
        this.turma = entity.getTurma() != null ? new TurmaDTO(entity.getTurma()) : null;
        this.dataInicio = entity.getDataInicio();
        this.dataFim = entity.getDataFim();
    }
}