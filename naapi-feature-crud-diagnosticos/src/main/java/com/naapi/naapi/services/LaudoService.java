package com.naapi.naapi.services;

import com.naapi.naapi.dtos.LaudoDTO;
import com.naapi.naapi.dtos.LaudoInsertDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Laudo;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.LaudoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<Laudo> list = repository.findByAlunoId(alunoId);
        return list.stream().map(LaudoDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public LaudoDTO insert(LaudoInsertDTO dto) {
        Laudo entity = new Laudo();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public LaudoDTO update(Long id, LaudoInsertDTO dto) {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Laudo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(LaudoInsertDTO dto, Laudo entity) {
        entity.setDataEmissao(dto.getDataEmissao());
        entity.setUrlArquivo(dto.getUrlArquivo());
        entity.setDescricao(dto.getDescricao());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        entity.setAluno(aluno);
    }
}