package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.PEI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeiDTO {

    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String metas;
    private String estrategias;
    private String avaliacao;

    private Long alunoId;
    private String alunoNome;
    private Long responsavelId;
    private String responsavelNome;

    public PeiDTO(PEI entity) {
        this.id = entity.getId();
        this.dataInicio = entity.getDataInicio();
        this.dataFim = entity.getDataFim();
        this.metas = entity.getMetas();
        this.estrategias = entity.getEstrategias();
        this.avaliacao = entity.getAvaliacao();

        if (entity.getAluno() != null) {
            this.alunoId = entity.getAluno().getId();
            this.alunoNome = entity.getAluno().getNome();
        }
        if (entity.getResponsavel() != null) {
            this.responsavelId = entity.getResponsavel().getId();
            this.responsavelNome = entity.getResponsavel().getNome();
        }
    }
}