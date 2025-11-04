package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TbAtendimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdAtendimentoRegistro") 
    private Long id;

    @Column(name = "DtHrAtendimento", nullable = false)
    private LocalDateTime dataHora; 

    @Column(name = "DsDescricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "DsStatus", length = 20)
    private String status; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdUsuarioResponsavel", nullable = false)
    private Usuario responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAtendimento", nullable = false)
    private TipoAtendimento tipoAtendimento;
}