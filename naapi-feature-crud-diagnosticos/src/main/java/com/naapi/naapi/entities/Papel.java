package com.naapi.naapi.entities;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "TbPapel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Papel implements GrantedAuthority, Serializable { 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdPapel") // Alterado de CdRole
    private Long id;

    @Column(name = "DsAuthority", unique = true, nullable = false)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}