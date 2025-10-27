package com.naapi.naapi.services;

import com.naapi.naapi.dtos.PapelDTO;
import com.naapi.naapi.dtos.UsuarioDTO;
import com.naapi.naapi.dtos.UsuarioInsertDTO;
import com.naapi.naapi.entities.Papel;
import com.naapi.naapi.entities.Usuario;
import com.naapi.naapi.repositories.PapelRepository;
import com.naapi.naapi.repositories.UsuarioRepository;
import com.naapi.naapi.services.exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PapelRepository papelRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = usuarioRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email não encontrado: " + email);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        List<Usuario> list = usuarioRepository.findAll();
        return list.stream().map(UsuarioDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO insert(UsuarioInsertDTO dto) {
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), -1L)) {
            throw new BusinessException("O email '" + dto.getEmail() + "' já está cadastrado.");
        }

        Usuario entity = new Usuario();
        copyDtoToEntity(dto, entity);

        entity.setSenha(passwordEncoder.encode(dto.getSenha()));

        entity = usuarioRepository.save(entity);
        return new UsuarioDTO(entity);
    }

    private void copyDtoToEntity(UsuarioInsertDTO dto, Usuario entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.getPapeis().clear();

        for (Long papelId : dto.getPapeis()) {
            Papel papel = papelRepository.findById(papelId)
                    .orElseThrow(() -> new EntityNotFoundException("Papel não encontrado com ID: " + papelId));
            entity.getPapeis().add(papel);
        }
    }

}