package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TbTipoAtendimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdAtendimento")
    private Long id;

    @Column(name = "NmAtendimento", nullable = false, unique = true, length = 45)
    private String nome;

    @OneToMany(mappedBy = "tipoAtendimento")
    @Builder.Default
    private Set<Atendimento> atendimentos = new HashSet<>();
}