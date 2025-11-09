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
public class TurmaService {

    private final TurmaRepository repository;

    @Transactional(readOnly = true)
    public List<TurmaDTO> findAll() {
        List<Turma> list = repository.findAll();
        return list.stream().map(TurmaDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TurmaDTO findById(Long id) {
        Turma entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));
        return new TurmaDTO(entity);
    }

    @Transactional
    public TurmaDTO insert(TurmaDTO dto) {
        if (repository.existsByNomeAndIdNot(dto.getNome(), -1L)) {
            throw new BusinessException("A turma '" + dto.getNome() + "' já está cadastrada.");
        }
        
        Turma entity = new Turma();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new TurmaDTO(entity);
    }

    @Transactional
    public TurmaDTO update(Long id, TurmaDTO dto) {
        Turma entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));

        if (repository.existsByNomeAndIdNot(dto.getNome(), id)) {
            throw new BusinessException("A turma '" + dto.getNome() + "' já está cadastrada.");
        }

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new TurmaDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Turma não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(TurmaDTO dto, Turma entity) {
        entity.setNome(dto.getNome());
    }
}