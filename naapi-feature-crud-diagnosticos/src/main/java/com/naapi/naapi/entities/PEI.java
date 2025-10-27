package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TbPEI")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PEI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPEI")
    private Long id;

    @Column(name = "DtInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "DtFim")
    private LocalDate dataFim;

    @Column(name = "DsMetas", columnDefinition = "TEXT")
    private String metas;

    @Column(name = "DsEstrategias", columnDefinition = "TEXT")
    private String estrategias;

    @Column(name = "DsAvaliacao", columnDefinition = "TEXT")
    private String avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdUsuarioResponsavel")
    private Usuario responsavel;
}