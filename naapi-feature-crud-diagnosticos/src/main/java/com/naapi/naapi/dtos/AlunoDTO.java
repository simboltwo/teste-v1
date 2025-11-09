package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- Importe o LocalDate ---
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {

    private Long id;
    private String nome;
    private String nomeSocial; // <--- CAMPO FALTAVA
    private String matricula;
    private String foto;
    private String prioridade;
    private Boolean ativo;

    // --- CAMPOS QUE FALTAVAM ---
    private LocalDate dataNascimento;
    private String cpf;
    private String telefoneEstudante;
    private Boolean provaOutroEspaco;
    private String processoSipac;
    private String anotacoesNaapi;
    private String adaptacoesNecessarias;
    private String necessidadesRelatoriosMedicos;
    // --- FIM DOS CAMPOS QUE FALTAVAM ---

    private CursoDTO curso;
    private TurmaDTO turma;

    @Builder.Default
    private Set<DiagnosticoDTO> diagnosticos = new HashSet<>();

    public AlunoDTO(Aluno entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.nomeSocial = entity.getNomeSocial(); // <--- LINHA FALTAVA
        this.matricula = entity.getMatricula();
        this.foto = entity.getFoto(); // <--- CAMPO DA FOTO
        this.prioridade = entity.getPrioridade();
        this.ativo = entity.getAtivo();

        // --- LINHAS QUE FALTAVAM NO CONSTRUTOR ---
        this.dataNascimento = entity.getDataNascimento();
        this.cpf = entity.getCpf();
        this.telefoneEstudante = entity.getTelefoneEstudante();
        this.provaOutroEspaco = entity.getProvaOutroEspaco();
        this.processoSipac = entity.getProcessoSipac();
        this.anotacoesNaapi = entity.getAnotacoesNaapi();
        this.adaptacoesNecessarias = entity.getAdaptacoesNecessarias();
        this.necessidadesRelatoriosMedicos = entity.getNecessidadesRelatoriosMedicos();
        // --- FIM DAS LINHAS QUE FALTAVAM ---

        if (entity.getCurso() != null) {
            this.curso = new CursoDTO(entity.getCurso());
        }
        if (entity.getTurma() != null) {
            this.turma = new TurmaDTO(entity.getTurma());
        }
        this.diagnosticos = entity.getDiagnosticos().stream()
                .map(DiagnosticoDTO::new)
                .collect(Collectors.toSet());
    }
}