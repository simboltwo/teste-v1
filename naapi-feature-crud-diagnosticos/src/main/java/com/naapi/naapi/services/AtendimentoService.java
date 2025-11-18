/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/services/AtendimentoService.java
 * Descrição: Modificado 'findByAlunoId' e adicionado 'findById' e 'findByResponsavelId'.
 */
package com.naapi.naapi.services;

import com.naapi.naapi.dtos.AtendimentoDTO;
import com.naapi.naapi.dtos.AtendimentoInsertDTO;
import com.naapi.naapi.dtos.AtendimentoStatusUpdateDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Atendimento;
import com.naapi.naapi.entities.TipoAtendimento;
import com.naapi.naapi.entities.Usuario;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.AtendimentoRepository;
import com.naapi.naapi.repositories.TipoAtendimentoRepository;
import com.naapi.naapi.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository repository;
    
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoAtendimentoRepository tipoAtendimentoRepository;

    // --- INÍCIO DA MUDANÇA ---
    @Transactional(readOnly = true)
    public AtendimentoDTO findById(Long id) {
        Atendimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado com ID: " + id));
        return new AtendimentoDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<AtendimentoDTO> findByAlunoId(Long alunoId, String status) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<Atendimento> list;
        if (status != null && !status.isBlank()) {
            list = repository.findByAlunoIdAndStatusOrderByDataHoraDesc(alunoId, status.toUpperCase());
        } else {
            list = repository.findByAlunoIdOrderByDataHoraDesc(alunoId);
        }
        
        return list.stream().map(AtendimentoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AtendimentoDTO> findByResponsavelId(Long responsavelId) {
        if (!usuarioRepository.existsById(responsavelId)) {
            throw new EntityNotFoundException("Usuário (responsável) não encontrado com ID: " + responsavelId);
        }
        List<Atendimento> list = repository.findByResponsavelIdOrderByDataHoraDesc(responsavelId);
        return list.stream().map(AtendimentoDTO::new).collect(Collectors.toList());
    }
    // --- FIM DA MUDANÇA ---

    @Transactional
    public AtendimentoDTO insert(AtendimentoInsertDTO dto) {
        Atendimento entity = new Atendimento();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AtendimentoDTO(entity);
    }

    @Transactional
    public AtendimentoDTO update(Long id, AtendimentoInsertDTO dto) {
        Atendimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado com ID: " + id));

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AtendimentoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Atendimento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
    

    @Transactional
    public AtendimentoDTO updateStatus(Long id, AtendimentoStatusUpdateDTO dto) {
        Atendimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado com ID: " + id));

        // Atualiza apenas o status
        entity.setStatus(dto.getStatus());

        entity = repository.save(entity);
        return new AtendimentoDTO(entity);
    }
    private void copyDtoToEntity(AtendimentoInsertDTO dto, Atendimento entity) {
        entity.setDataHora(dto.getDataHora());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário responsável não encontrado com ID: " + dto.getResponsavelId()));

        TipoAtendimento tipo = tipoAtendimentoRepository.findById(dto.getTipoAtendimentoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Atendimento não encontrado com ID: " + dto.getTipoAtendimentoId()));

        entity.setAluno(aluno);
        entity.setResponsavel(responsavel);
        entity.setTipoAtendimento(tipo);
    }
}