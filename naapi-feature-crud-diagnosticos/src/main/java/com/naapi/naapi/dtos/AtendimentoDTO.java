package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Atendimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtendimentoDTO {

    private Long id;
    private LocalDateTime dataHora;
    private String descricao;
    private String status;

    private Long alunoId;
    private String alunoNome;
    private Long responsavelId;
    private String responsavelNome;
    private Long tipoAtendimentoId;
    private String tipoAtendimentoNome;

    public AtendimentoDTO(Atendimento entity) {
        this.id = entity.getId();
        this.dataHora = entity.getDataHora();
        this.descricao = entity.getDescricao();
        this.status = entity.getStatus();

        if (entity.getAluno() != null) {
            this.alunoId = entity.getAluno().getId();
            this.alunoNome = entity.getAluno().getNome();
        }
        if (entity.getResponsavel() != null) {
            this.responsavelId = entity.getResponsavel().getId();
            this.responsavelNome = entity.getResponsavel().getNome();
        }
        if (entity.getTipoAtendimento() != null) {
            this.tipoAtendimentoId = entity.getTipoAtendimento().getId();
            this.tipoAtendimentoNome = entity.getTipoAtendimento().getNome();
        }
    }
}