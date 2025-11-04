package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // Importar
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
    private String nomeSocial;
    private String matricula;
    private String cpf; // NOVO
    private LocalDate dataNascimento; // NOVO
    private String serie; // NOVO
    private String foto;
    private String prioridade; // ALTERADO (era Boolean prioridadeAtendimento)
    private String nomeProtegido; // NOVO
    private Boolean provaOutroEspaco; // NOVO
    private String adaptacoesNecessarias; // NOVO
    private Boolean possuiPEI; // NOVO
    private String telefoneEstudante; // NOVO
    private String telefoneResponsavel; // NOVO
    private String tipoAtendimentoPrincipal; // NOVO
    private String assistenteReferencia; // NOVO
    private String membroNaapiReferencia; // NOVO
    private String processoSipac; // NOVO
    private Boolean paisAutorizados; // NOVO
    private String anotacoesNaapi; // NOVO
    private String necessidadesRelatoriosMedicos; // NOVO
    private LocalDate dataUltimoLaudo; // NOVO
    private Boolean ativo;

    private CursoDTO curso;
    private TurmaDTO turma;

    @Builder.Default
    private Set<DiagnosticoDTO> diagnosticos = new HashSet<>();

    public AlunoDTO(Aluno entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.nomeSocial = entity.getNomeSocial();
        this.matricula = entity.getMatricula();
        this.cpf = entity.getCpf(); // NOVO
        this.dataNascimento = entity.getDataNascimento(); // NOVO
        this.serie = entity.getSerie(); // NOVO
        this.foto = entity.getFoto();
        this.prioridade = entity.getPrioridade(); // ALTERADO
        this.nomeProtegido = entity.getNomeProtegido(); // NOVO
        this.provaOutroEspaco = entity.getProvaOutroEspaco(); // NOVO
        this.adaptacoesNecessarias = entity.getAdaptacoesNecessarias(); // NOVO
        this.possuiPEI = entity.getPossuiPEI(); // NOVO
        this.telefoneEstudante = entity.getTelefoneEstudante(); // NOVO
        this.telefoneResponsavel = entity.getTelefoneResponsavel(); // NOVO
        this.tipoAtendimentoPrincipal = entity.getTipoAtendimentoPrincipal(); // NOVO
        this.assistenteReferencia = entity.getAssistenteReferencia(); // NOVO
        this.membroNaapiReferencia = entity.getMembroNaapiReferencia(); // NOVO
        this.processoSipac = entity.getProcessoSipac(); // NOVO
        this.paisAutorizados = entity.getPaisAutorizados(); // NOVO
        this.anotacoesNaapi = entity.getAnotacoesNaapi(); // NOVO
        this.necessidadesRelatoriosMedicos = entity.getNecessidadesRelatoriosMedicos(); // NOVO
        this.dataUltimoLaudo = entity.getDataUltimoLaudo(); // NOVO
        this.ativo = entity.getAtivo();

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