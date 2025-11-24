package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TbHistoricoAcademico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdHistorico")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CdAluno", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "CdCurso", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "CdTurma")
    private Turma turma;

    @Column(name = "DtInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "DtFim")
    private LocalDate dataFim;
}