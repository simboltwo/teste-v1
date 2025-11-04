package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate; // Importar LocalDate
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
    
    @Column(name = "NmCpf", unique = true, length = 20) // NOVO
    private String cpf;

    @Column(name = "DtNascimento") // NOVO
    private LocalDate dataNascimento; 

    @Column(name = "DsSerie", length = 10) // NOVO
    private String serie; 

    @Column(name = "DsFotoUrl")
    private String foto;

    @Column(name = "DsPrioridade", length = 20) // ALTERADO (era Boolean prioridadeAtendimento)
    private String prioridade; 

    @Column(name = "NmProtegido", length = 100) // NOVO
    private String nomeProtegido;

    @Column(name = "IcProvaOutroEspaco") // NOVO
    private Boolean provaOutroEspaco;

    @Column(name = "DsAdaptacoesNecessarias", columnDefinition = "TEXT") // NOVO
    private String adaptacoesNecessarias; 

    @Column(name = "IcPossuiPEI") // NOVO
    private Boolean possuiPEI;

    @Column(name = "DsTelefoneEstudante", length = 20) // NOVO
    private String telefoneEstudante;

    @Column(name = "DsTelefoneResponsavel", length = 20) // NOVO
    private String telefoneResponsavel;

    @Column(name = "DsTipoAtendimentoPrincipal", length = 50) // NOVO
    private String tipoAtendimentoPrincipal;

    @Column(name = "NmAssistenteReferencia", length = 100) // NOVO
    private String assistenteReferencia;
    
    @Column(name = "NmMembroNaapiReferencia", length = 100) // NOVO
    private String membroNaapiReferencia;
    
    @Column(name = "CdProcessoSipac", unique = true, length = 30) // NOVO
    private String processoSipac;
    
    @Column(name = "IcPaisAutorizados") // NOVO
    private Boolean paisAutorizados;
    
    @Column(name = "DsAnotacoesNaapi", columnDefinition = "TEXT") // NOVO
    private String anotacoesNaapi;
    
    @Column(name = "DsNecessidadesRelatoriosMedicos", columnDefinition = "TEXT") // NOVO
    private String necessidadesRelatoriosMedicos;
    
    @Column(name = "DtUltimoLaudo") // NOVO
    private LocalDate dataUltimoLaudo;

    @Builder.Default
    @Column(name = "IcAtivo", nullable = false)
    private Boolean ativo = true;

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
}