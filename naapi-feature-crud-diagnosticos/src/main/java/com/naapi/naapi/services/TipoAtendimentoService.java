package com.naapi.naapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naapi.naapi.services.exceptions.BusinessException;
import com.naapi.naapi.dtos.*;
import com.naapi.naapi.entities.*;
import com.naapi.naapi.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoAtendimentoService {
    
    private final TipoAtendimentoRepository repository;

    @Transactional(readOnly = true)
    public List<TipoAtendimentoDTO> findAll() {
        List<TipoAtendimento> list = repository.findAll();
        return list.stream().map(TipoAtendimentoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoAtendimentoDTO findById(Long id) {
        TipoAtendimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Atendimento não encontrado com ID: " + id));
        return new TipoAtendimentoDTO(entity);
    }

    @Transactional
    public TipoAtendimentoDTO insert(TipoAtendimentoDTO dto) {
        if (repository.existsByNomeAndIdNot(dto.getNome(), -1L)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }
        
        TipoAtendimento entity = new TipoAtendimento();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new TipoAtendimentoDTO(entity);
    }

    @Transactional
    public TipoAtendimentoDTO update(Long id, TipoAtendimentoDTO dto) {
        TipoAtendimento entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Atendimento não encontrado com ID: " + id));

        if (repository.existsByNomeAndIdNot(dto.getNome(), id)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new TipoAtendimentoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de Atendimento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(TipoAtendimentoDTO dto, TipoAtendimento entity) {
        entity.setNome(dto.getNome());
    }
}
