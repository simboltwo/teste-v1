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
    private String nome;
    private String nomeSocial;
    private String foto;
    private LocalDate dataNascimento;
    private String cpf;
    private String telefoneEstudante;

    @NotBlank(message = "A matrícula é obrigatória.")
    private String matricula;
    private String prioridade; // "Baixa", "Média", "Alta"
    private Boolean provaOutroEspaco;
    private String processoSipac;
    @NotNull
    private Long cursoId;
    @NotNull
    private Long turmaId;
    
    // --- Etapa 3: Dados NAAPI ---
    private Set<Long> diagnosticosId;
    private String anotacoesNaapi;
    private String adaptacoesNecessarias;
    private String necessidadesRelatoriosMedicos;
}