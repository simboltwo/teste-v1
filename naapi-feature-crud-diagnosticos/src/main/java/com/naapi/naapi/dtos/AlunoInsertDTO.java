package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoInsertDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    private String nomeSocial;

    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20, message = "A matrícula não pode exceder 20 caracteres.")
    private String matricula;

    private String foto;
    
    private Boolean prioridadeAtendimento;

    @NotNull(message = "O ID do curso é obrigatório.")
    private Long cursoId;

    @NotNull(message = "O ID da turma é obrigatório.")
    private Long turmaId;

    private Set<Long> diagnosticosId;
}