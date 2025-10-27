package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Turma;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurmaDTO {
    
    private Long id;

    @Size(min = 3, max = 45, message = "O nome da turma deve ter entre 3 e 45 caracteres.")
    private String nome;

    public TurmaDTO(Turma entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }
}
