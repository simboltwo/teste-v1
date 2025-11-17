package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // --- INÍCIO DA CORREÇÃO ---
    // 1. Este é o método que o Spring Security (via UsuarioService) usa.
    // O Spring Data JPA entende "findByEmail" e busca pelo campo "email".
    // O nome "loadUserByUsername" estava causando o crash.
    UserDetails findByEmail(String email); //
    // --- FIM DA CORREÇÃO ---

    // 2. Este é o NOVO método que o DataSeedController usará.
    // Ele retorna a entidade completa, permitindo a alteração da senha.
    Usuario findUsuarioByEmail(String email);

    // 3. Este método estava correto e permanece.
    boolean existsByEmailAndIdNot(String email, Long id); //
}