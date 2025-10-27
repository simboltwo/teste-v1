package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TbTurma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdTurma")
    private Long id;

    @Column(name = "NmTurma", length = 45)
    private String nome;
}
