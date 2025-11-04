package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String nomeSocial;
    private String matricula;
    private String cpf;
    private LocalDate dataNascimento;
    private String serie;
    private String foto; // A API continua enviando a URL da foto
    private String prioridade;
    private String nomeProtegido;
    private Boolean provaOutroEspaco;
    private String adaptacoesNecessarias; // Campo Corrigido
    private Boolean possuiPEI;
    private String telefoneEstudante;
    
    // Campos de String para os selects NAAPI (para leitura fácil no frontend)
    private String tipoAtendimentoPrincipal;
    private String assistenteReferencia;
    private String membroNaapiReferencia;
    
    private String processoSipac;
    private String anotacoesNaapi;
    private String necessidadesRelatoriosMedicos;
    private LocalDate dataUltimoLaudo;
    private Boolean ativo;
    
    private CursoDTO curso;
    private TurmaDTO turma;
    
    @Builder.Default
    private Set<DiagnosticoDTO> diagnosticos = new HashSet<>();

    // --- NOVO CAMPO ---
    @Builder.Default
    private Set<ResponsavelDTO> responsaveis = new HashSet<>();

    public AlunoDTO(Aluno entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.nomeSocial = entity.getNomeSocial();
        this.matricula = entity.getMatricula();
        this.cpf = entity.getCpf();
        this.dataNascimento = entity.getDataNascimento();
        this.serie = entity.getSerie();
        this.foto = entity.getFoto();
        this.prioridade = entity.getPrioridade();
        this.nomeProtegido = entity.getNomeProtegido();
        this.provaOutroEspaco = entity.getProvaOutroEspaco();
        this.adaptacoesNecessarias = entity.getAdaptacoesNecessarias(); // Corrigido
        this.possuiPEI = entity.getPossuiPEI();
        this.telefoneEstudante = entity.getTelefoneEstudante();
        this.processoSipac = entity.getProcessoSipac();
        this.anotacoesNaapi = entity.getAnotacoesNaapi();
        this.necessidadesRelatoriosMedicos = entity.getNecessidadesRelatoriosMedicos();
        this.dataUltimoLaudo = entity.getDataUltimoLaudo();
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

        // --- LÓGICA DE POPULAÇÃO ATUALIZADA ---
        
        // 1. Popula a lista de responsáveis
        if (entity.getResponsaveis() != null) {
            this.responsaveis = entity.getResponsaveis().stream()
                    .map(ResponsavelDTO::new)
                    .collect(Collectors.toSet());
        }
        
        // 2. Popula os campos de string a partir dos relacionamentos (com null check)
        if (entity.getTipoAtendimentoPrincipal() != null) {
            this.tipoAtendimentoPrincipal = entity.getTipoAtendimentoPrincipal().getNome();
        }
        if (entity.getAssistenteReferencia() != null) {
            this.assistenteReferencia = entity.getAssistenteReferencia().getNome();
        }
        if (entity.getMembroNaapiReferencia() != null) {
            this.membroNaapiReferencia = entity.getMembroNaapiReferencia().getNome();
        }
    }
}