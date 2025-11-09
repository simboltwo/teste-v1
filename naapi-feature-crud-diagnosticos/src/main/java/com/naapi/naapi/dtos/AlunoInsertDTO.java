package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // Importar
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoInsertDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100)
    private String nome;

    private String nomeSocial;
    private String foto;
    
    // --- NOVOS CAMPOS ADICIONADOS ---
    private LocalDate dataNascimento;
    
    private String cpf;
    
    private String telefoneEstudante;
    
    private Boolean provaOutroEspaco; 
    // --- FIM NOVOS CAMPOS ---

    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20)
    private String matricula;

    // --- CAMPO ATUALIZADO ---
    // Era: private Boolean prioridadeAtendimento;
    private String prioridade; // Agora é String: "Baixa", "Média", "Alta"

    @NotNull(message = "O ID do curso é obrigatório.")
    private Long cursoId;

    @NotNull(message = "O ID da turma é obrigatório.")
    private Long turmaId;

    private Set<Long> diagnosticosId;
}