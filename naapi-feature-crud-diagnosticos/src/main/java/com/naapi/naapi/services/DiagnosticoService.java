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
public class DiagnosticoService {
    
    private final DiagnosticoRepository repository;

    @Transactional(readOnly = true)
    public List<DiagnosticoDTO> findAll() {
        List<Diagnostico> list = repository.findAll();
        return list.stream().map(DiagnosticoDTO::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DiagnosticoDTO findById(Long id) {
        Diagnostico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostico não encontrado com ID: " + id));
        return new DiagnosticoDTO(entity);
    }

    @Transactional
    public DiagnosticoDTO insert(DiagnosticoDTO dto) {
        if (dto.getCid() != null && !dto.getCid().isBlank() && repository.existsByCidAndIdNot(dto.getCid(), -1L)) {
            throw new BusinessException("O CID '" + dto.getCid() + "' já está cadastrado.");
        }
        if (repository.existsByNomeAndIdNot(dto.getNome(), -1L)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }
        if (dto.getSigla() != null && !dto.getSigla().isBlank() && repository.existsBySiglaAndIdNot(dto.getSigla(), -1L)) {
            throw new BusinessException("A sigla '" + dto.getSigla() + "' já está cadastrada.");
        }

        Diagnostico entity = new Diagnostico();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new DiagnosticoDTO(entity);
    }

    @Transactional
    public DiagnosticoDTO update(Long id, DiagnosticoDTO dto) {
        Diagnostico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Diagnostico não encontrado com ID: " + id));

        if (dto.getCid() != null && !dto.getCid().isBlank() && repository.existsByCidAndIdNot(dto.getCid(), id)) {
            throw new BusinessException("O CID '" + dto.getCid() + "' já está cadastrado.");
        }
        if (repository.existsByNomeAndIdNot(dto.getNome(), id)) {
            throw new BusinessException("O nome '" + dto.getNome() + "' já está cadastrado.");
        }
        if (dto.getSigla() != null && !dto.getSigla().isBlank() && repository.existsBySiglaAndIdNot(dto.getSigla(), id)) {
            throw new BusinessException("A sigla '" + dto.getSigla() + "' já está cadastrada.");
        }

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new DiagnosticoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Diagnostico não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(DiagnosticoDTO dto, Diagnostico entity) {
        entity.setNome(dto.getNome());
        entity.setCid(dto.getCid());
        entity.setSigla(dto.getSigla());
    }
}
