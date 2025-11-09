package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDate; // Importar
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

    @Column(name = "DsFotoUrl")
    private String foto;

    // --- CAMPO ATUALIZADO ---
    // Era: @Column(name = "IcPrioridade") private Boolean prioridadeAtendimento;
    @Column(name = "DsPrioridade", length = 10) // Ex: "Baixa", "Média", "Alta"
    private String prioridade; 

    @Builder.Default
    @Column(name = "IcAtivo", nullable = false)
    private Boolean ativo = true;

    // --- NOVOS CAMPOS ADICIONADOS ---
    @Column(name = "DtNascimento")
    private LocalDate dataNascimento;

    @Column(name = "DsCpf", length = 14)
    private String cpf;

    @Column(name = "DsTelefoneEstudante", length = 20)
    private String telefoneEstudante;

    @Column(name = "IcProvaOutroEspaco")
    private Boolean provaOutroEspaco;

    @Column(name = "DsProcessoSipac", length = 30)
    private String processoSipac;

    @Column(name = "DsAnotacoesNaapi", columnDefinition = "TEXT")
    private String anotacoesNaapi;
    
    @Column(name = "DsAdaptacoes", columnDefinition = "TEXT")
    private String adaptacoesNecessarias;

    @Column(name = "DsNecessidadesMedicas", columnDefinition = "TEXT")
    private String necessidadesRelatoriosMedicos;

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