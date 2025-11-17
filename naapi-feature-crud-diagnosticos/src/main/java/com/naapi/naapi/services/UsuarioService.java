package com.naapi.naapi.services;

import com.naapi.naapi.dtos.PapelDTO;
import com.naapi.naapi.dtos.UsuarioDTO;
import com.naapi.naapi.dtos.UsuarioInsertDTO;
import com.naapi.naapi.dtos.UsuarioUpdateDTO;
// Imports dos novos DTOs
import com.naapi.naapi.dtos.UsuarioPasswordUpdateDTO;
import com.naapi.naapi.dtos.UsuarioSelfUpdateDTO;
import com.naapi.naapi.entities.Papel;
import com.naapi.naapi.entities.Usuario;
import com.naapi.naapi.repositories.PapelRepository;
import com.naapi.naapi.repositories.UsuarioRepository;
import com.naapi.naapi.services.exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        return new UsuarioDTO(entity);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO getSelf() {
        Usuario entity = getAuthenticatedUser();
        // Usamos findById para garantir que o DTO venha populado corretamente
        return findById(entity.getId());
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

    @Transactional
    public UsuarioDTO update(Long id, UsuarioUpdateDTO dto) {
        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BusinessException("O email '" + dto.getEmail() + "' já está cadastrado.");
        }

        copyDtoToEntity(dto, entity);
        entity = usuarioRepository.save(entity);
        return new UsuarioDTO(entity);
    }

    // --- NOVO MÉTODO PARA ATUALIZAR DETALHES (NOME/EMAIL) ---
    @Transactional
    public UsuarioDTO updateSelfDetails(UsuarioSelfUpdateDTO dto) {
        Usuario entity = getAuthenticatedUser();
        
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), entity.getId())) {
            throw new BusinessException("O email '" + dto.getEmail() + "' já está cadastrado.");
        }
        
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        
        entity = usuarioRepository.save(entity);
        return new UsuarioDTO(entity);
    }

    // --- NOVO MÉTODO PARA ATUALIZAR SENHA ---
    @Transactional
    public void updateSelfPassword(UsuarioPasswordUpdateDTO dto) {
        Usuario entity = getAuthenticatedUser();

        // 1. Validar se a senha atual bate
        if (!passwordEncoder.matches(dto.getSenhaAtual(), entity.getSenha())) {
            throw new BusinessException("A 'Senha Atual' está incorreta.");
        }

        // 2. Validar se a nova senha e a confirmação batem
        if (!dto.getNovaSenha().equals(dto.getConfirmacaoNovaSenha())) {
            throw new BusinessException("A 'Nova Senha' e a 'Confirmação' não são iguais.");
        }

        // 3. Salvar a nova senha encriptada
        entity.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // (Helper para Admin Insert)
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

    // (Helper para Admin Update)
    private void copyDtoToEntity(UsuarioUpdateDTO dto, Usuario entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.getPapeis().clear();

        for (Long papelId : dto.getPapeis()) {
            Papel papel = papelRepository.findById(papelId)
                    .orElseThrow(() -> new EntityNotFoundException("Papel não encontrado com ID: " + papelId));
            entity.getPapeis().add(papel);
        }
    }

    protected Usuario getAuthenticatedUser() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            UserDetails userDetails = usuarioRepository.findByEmail(username);
            if (userDetails == null) {
                throw new UsernameNotFoundException("Usuário não encontrado no contexto de segurança");
            }
            return (Usuario) userDetails;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Não foi possível obter o usuário autenticado");
        }
    }
}