package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Laudo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaudoDTO {

    private Long id;
    private LocalDate dataEmissao;
    private String urlArquivo;
    private String descricao;
    private Long alunoId;

    public LaudoDTO(Laudo entity) {
        this.id = entity.getId();
        this.dataEmissao = entity.getDataEmissao();
        this.urlArquivo = entity.getUrlArquivo();
        this.descricao = entity.getDescricao();
        if (entity.getAluno() != null) {
            this.alunoId = entity.getAluno().getId();
        }
    }
}