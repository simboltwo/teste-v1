package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Responsavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsavelDTO {

    private Long id;
    private String nome;
    private String parentesco;
    private String telefone;
    private Boolean autorizadoBuscar;

    public ResponsavelDTO(Responsavel entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.parentesco = entity.getParentesco();
        this.telefone = entity.getTelefone();
        this.autorizadoBuscar = entity.getAutorizadoBuscar();
    }
}