package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.TipoAtendimento;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAtendimentoDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
    private String nome;

    public TipoAtendimentoDTO(TipoAtendimento entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }
}