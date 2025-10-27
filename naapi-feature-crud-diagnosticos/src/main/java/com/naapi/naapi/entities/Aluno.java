package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TbAluno")
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

    @Column(name = "IcPrioridade")
    private Boolean prioridadeAtendimento;

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