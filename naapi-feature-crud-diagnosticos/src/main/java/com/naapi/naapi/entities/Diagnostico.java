package com.naapi.naapi.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TbDiagnostico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnostico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdDiagnostico")
    private Long id;

    @Column(name = "cid", unique = true)
    private String cid;

    @Column(name = "NmDiagnostico", nullable = false, length = 45)
    private String nome;

    @Column(name = "SIGLA", length = 10)
    private String sigla;
}