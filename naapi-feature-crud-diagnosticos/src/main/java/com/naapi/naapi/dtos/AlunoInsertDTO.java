// src/main/java/com/naapi/naapi/dtos/AlunoInsertDTO.java
package com.naapi.naapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoInsertDTO {

    // ... (nome, nomeSocial, matricula, cpf, dataNascimento, serie, foto, prioridade - sem mudanças)
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;
    private String nomeSocial;
    @NotBlank(message = "A matrícula é obrigatória.")
    @Size(max = 20, message = "A matrícula não pode exceder 20 caracteres.")
    private String matricula;
    @Size(max = 20, message = "O CPF não pode exceder 20 caracteres.")
    private String cpf; 
    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;
    @Size(max = 10, message = "A série não pode exceder 10 caracteres.")
    private String serie;
    
    // Este campo 'foto' agora é apenas para o DTO de *retorno*.
    // O DTO de *inserção* não o terá, pois a foto virá via MultipartFile.
    // private String foto; 
    
    @Size(max = 20, message = "A prioridade não pode exceder 20 caracteres.")
    private String prioridade;

    // ... (provaOutroEspaco, possuiPEI - sem mudanças)
    private Boolean provaOutroEspaco;
    private Boolean possuiPEI;
    
    @Size(max = 20)
    private String telefoneEstudante;

    // --- CAMPO REMOVIDO ---
    // private String telefoneResponsavel;

    // --- CAMPOS REMOVIDOS ---
    // private String tipoAtendimentoPrincipal;
    // private String assistenteReferencia;
    // private String membroNaapiReferencia;
    
    @Size(max = 30, message = "O Processo Sipac não pode exceder 30 caracteres.")
    private String processoSipac; 
    
    // --- CAMPO REMOVIDO ---
    // private Boolean paisAutorizados;
    
    // ... (anotacoesNaapi, necessidadesRelatoriosMedicos, dataUltimoLaudo - sem mudanças)
    private String anotacoesNaapi;
    private String necessidadesRelatoriosMedicos;
    private LocalDate dataUltimoLaudo;

    // ... (cursoId, turmaId, diagnosticosId - sem mudanças)
    @NotNull(message = "O ID do curso é obrigatório.")
    private Long cursoId;
    @NotNull(message = "O ID da turma é obrigatório.")
    private Long turmaId;
    private Set<Long> diagnosticosId;

    // --- NOVOS CAMPOS ADICIONADOS ---
    
    // 1. Lista de Responsáveis
    @Builder.Default
    private Set<ResponsavelInsertDTO> responsaveis = new HashSet<>();

    // 2. IDs dos campos de seleção NAAPI
    private Long tipoAtendimentoPrincipalId;
    private Long assistenteReferenciaId;
    private Long membroNaapiReferenciaId;
}