package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TbLaudo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Laudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdLaudo")
    private Long id;

    @Column(name = "DtEmissao")
    private LocalDate dataEmissao;

    @Column(name = "DsUrlArquivo", nullable = false)
    private String urlArquivo; 
    @Column(name = "DsLaudo", columnDefinition = "TEXT")
    private String descricao; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAluno", nullable = false)
    private Aluno aluno;
}