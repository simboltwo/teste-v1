package com.naapi.naapi.dtos;

import jakarta.persistence.Column;
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

    // --- Etapa 1: Dados Pessoais ---
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100)
    private String nome;
    
    private String nomeSocial;
    private String foto; // O serviço de upload irá preencher isto
    private LocalDate dataNascimento;
    private String cpf;
    private String telefoneEstudante;

    // --- Etapa 2: Dados Académicos ---
    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20)
    private String matricula;

    private String prioridade; // "Baixa", "Média", "Alta"
    private Boolean provaOutroEspaco;
    private String processoSipac;
    
    @NotNull(message = "O ID do curso é obrigatório.")
    private Long cursoId;

    @NotNull(message = "O ID da turma é obrigatório.")
    private Long turmaId;
    
    // --- Etapa 3: Dados NAAPI ---
    private Set<Long> diagnosticosId;
    private String anotacoesNaapi;
    private String adaptacoesNecessarias;
    private String necessidadesRelatoriosMedicos;
}