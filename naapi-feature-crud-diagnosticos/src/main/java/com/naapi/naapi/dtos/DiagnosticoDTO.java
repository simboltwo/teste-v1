package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Diagnostico;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoDTO {
    
    private Long id;

    // --- MUDANÇA AQUI ---
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres.") // Alterado de 45 para 255
    private String nome;

    @Size(max = 10, message = "O CID não pode ultrapassar 10 caracteres.")
    private String cid;

    @Size(max = 10, message = "A sigla não pode ultrapassar 10 caracteres.")
    private String sigla;

    public DiagnosticoDTO(Diagnostico entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.cid = entity.getCid();
        this.sigla = entity.getSigla();
    }
}