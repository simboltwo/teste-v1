package com.naapi.naapi.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TbUsuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CdUsuario")
    private Long id;

    @Column(name = "NmUsuario", nullable = false)
    private String nome;

    @Column(name = "DsEmail", unique = true, nullable = false)
    private String email;

    @Column(name = "DsSenha", nullable = false)
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TbUsuarioPapel",
            joinColumns = @JoinColumn(name = "CdUsuario"),
            inverseJoinColumns = @JoinColumn(name = "CdPapel"))
    @Builder.Default
    private Set<Papel> papeis = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return papeis;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "responsavel")
    @Builder.Default
    private Set<Atendimento> atendimentosRealizados = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TbProfessorTurma",
            joinColumns = @JoinColumn(name = "CdUsuario"),
            inverseJoinColumns = @JoinColumn(name = "CdTurma"))
    @Builder.Default
    private Set<Turma> turmasLecionadas = new HashSet<>();
}