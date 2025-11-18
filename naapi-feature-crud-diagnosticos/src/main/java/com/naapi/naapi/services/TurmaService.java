/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/services/TurmaService.java
 * Descrição: Injetado AlunoRepository e adicionada verificação no método delete.
 */
package com.naapi.naapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naapi.naapi.services.exceptions.BusinessException;
import com.naapi.naapi.dtos.*;
import com.naapi.naapi.entities.*;
import com.naapi.naapi.repositories.*; // Importar AlunoRepository
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository repository;
    private final AlunoRepository alunoRepository; // --- INÍCIO DA MUDANÇA (Injeção) ---

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

        // --- INÍCIO DA MUDANÇA (Verificação) ---
        if (alunoRepository.existsByTurmaId(id)) {
            throw new BusinessException("Não é possível excluir esta turma, pois ela já está vinculada a alunos.");
        }
        // --- FIM DA MUDANÇA ---
        
        repository.deleteById(id);
    }

    private void copyDtoToEntity(TurmaDTO dto, Turma entity) {
        entity.setNome(dto.getNome());
    }
}