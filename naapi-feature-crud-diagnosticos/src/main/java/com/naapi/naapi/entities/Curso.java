package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TbCurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdCurso")
    private Long id;

    @Column(name = "NmCurso", length = 45)
    private String nome;
}