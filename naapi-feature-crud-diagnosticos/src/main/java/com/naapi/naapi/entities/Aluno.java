package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TbAluno")
@Where(clause = "ic_ativo = true")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdAluno")
    private Long id;

    @Column(name = "NmAluno", nullable = false, length = 100)
    private String nome;
    @Column(name = "NmSocial", length = 100)
    private String nomeSocial;
    @Column(name = "CdMatricula", unique = true, nullable = false, length = 20)
    private String matricula;
    @Column(name = "NmCpf", unique = true, length = 20)
    private String cpf;
    @Column(name = "DtNascimento")
    private LocalDate dataNascimento;
    @Column(name = "DsSerie", length = 10)
    private String serie;
    @Column(name = "DsFotoUrl")
    private String foto;
    @Column(name = "DsPrioridade", length = 20)
    private String prioridade;
    
    @Column(name = "NmProtegido", length = 100)
    private String nomeProtegido;
    @Column(name = "IcProvaOutroEspaco")
    private Boolean provaOutroEspaco;
    
    // --- LINHA CORRIGIDA: Esta linha estava faltando ---
    @Column(name = "DsAdaptacoesNecessarias", columnDefinition = "TEXT")
    private String adaptacoesNecessarias;
    
    @Column(name = "IcPossuiPEI")
    private Boolean possuiPEI;
    
    @Column(name = "DsTelefoneEstudante", length = 20)
    private String telefoneEstudante;

    @Column(name = "CdProcessoSipac", unique = true, length = 30)
    private String processoSipac;
    
    @Column(name = "DsAnotacoesNaapi", columnDefinition = "TEXT")
    private String anotacoesNaapi;
    @Column(name = "DsNecessidadesRelatoriosMedicos", columnDefinition = "TEXT")
    private String necessidadesRelatoriosMedicos;
    @Column(name = "DtUltimoLaudo")
    private LocalDate dataUltimoLaudo;

    @Builder.Default
    @Column(name = "IcAtivo", nullable = false)
    private Boolean ativo = true;

    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "CdCurso")
    private Curso curso;
    @ManyToOne
    @JoinColumn(name = "CdTurma")
    private Turma turma;

    @ManyToMany
    @JoinTable(name = "TbAlunoDiagnostico",
            joinColumns = @JoinColumn(name = "CdAluno"),
            inverseJoinColumns = @JoinColumn(name = "CdDiagnostico"))
    @Builder.Default
    private Set<Diagnostico> diagnosticos = new HashSet<>();

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Laudo> laudos = new HashSet<>();
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PEI> peis = new HashSet<>();
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Atendimento> atendimentos = new HashSet<>();

    // --- NOVOS RELACIONAMENTOS ---
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Responsavel> responsaveis = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdTipoAtendimentoPrincipal")
    private TipoAtendimento tipoAtendimentoPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAssistenteReferencia")
    private Usuario assistenteReferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdMembroNaapiReferencia")
    private Usuario membroNaapiReferencia;
}