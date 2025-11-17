package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails loadUserByUsername(String email); // <- DEIXE ESTE COMO ESTÁ (para o Spring Security)

    // --- INÍCIO DA ADIÇÃO ---
    // Adicione este novo método para usarmos no DataSeedController
    Usuario findByEmail(String email);
    // --- FIM DA ADIÇÃO ---

    boolean existsByEmailAndIdNot(String email, Long id);
}