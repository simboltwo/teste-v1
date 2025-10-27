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
public class CursoService {

    private final CursoRepository repository;

    @Transactional(readOnly = true)
    public List<CursoDTO> findAll() {
        List<Curso> list = repository.findAll();
        return list.stream().map(CursoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoDTO findById(Long id) {
        Curso entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));
        return new CursoDTO(entity);
    }

    @Transactional
    public CursoDTO insert(CursoDTO dto) {
        if (repository.existsByNomeAndIdNot(dto.getNome(), -1L)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }
        
        Curso entity = new Curso();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new CursoDTO(entity);
    }

    @Transactional
    public CursoDTO update(Long id, CursoDTO dto) {
        Curso entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));

        if (repository.existsByNomeAndIdNot(dto.getNome(), id)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new CursoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(CursoDTO dto, Curso entity) {
        entity.setNome(dto.getNome());
    }
}
