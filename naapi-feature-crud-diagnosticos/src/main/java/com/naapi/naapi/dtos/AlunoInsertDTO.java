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

    @Size(max = 20, message = "O CPF não pode exceder 20 caracteres.")
    private String cpf; 

    @Past(message = "A data de nascimento deve ser no passado.")
    private LocalDate dataNascimento;

    @Size(max = 10, message = "A série não pode exceder 10 caracteres.")
    private String serie;

    private String foto;
    
    @Size(max = 20, message = "A prioridade não pode exceder 20 caracteres.")
    private String prioridade;

    // private String nomeProtegido; // REMOVIDO
    private Boolean provaOutroEspaco;
    private String adaptacoesNecessarias;
    private Boolean possuiPEI;
    private String telefoneEstudante;
    private String telefoneResponsavel;
    private String tipoAtendimentoPrincipal;
    private String assistenteReferencia;
    private String membroNaapiReferencia;
    
    @Size(max = 30, message = "O Processo Sipac não pode exceder 30 caracteres.")
    private String processoSipac; 
    
    private Boolean paisAutorizados;
    private String anotacoesNaapi;
    private String necessidadesRelatoriosMedicos;
    private LocalDate dataUltimoLaudo;

    @NotNull(message = "O ID do curso é obrigatório.")
    private Long cursoId;

    @NotNull(message = "O ID da turma é obrigatório.")
    private Long turmaId;

    private Set<Long> diagnosticosId;
}