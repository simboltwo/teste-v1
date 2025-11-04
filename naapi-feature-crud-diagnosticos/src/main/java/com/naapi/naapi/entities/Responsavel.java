package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TbResponsavel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Responsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdResponsavel")
    private Long id;

    @Column(name = "NmResponsavel", nullable = false, length = 100)
    private String nome;

    @Column(name = "DsParentesco", length = 50)
    private String parentesco; // Ex: "Pai", "Mãe", "Outro"

    @Column(name = "DsTelefone", length = 20)
    private String telefone;

    @Column(name = "IcAutorizadoBuscar")
    private Boolean autorizadoBuscar; // O checkbox que você pediu

    // Relacionamento de volta para o Aluno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CdAluno", nullable = false)
    private Aluno aluno;
}