package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Curso;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {
    
    private Long id;

    @Size(min = 3, max = 45, message = "O nome do curso deve ter entre 3 e 45 caracteres.")
    private String nome;

    public CursoDTO(Curso entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }
}