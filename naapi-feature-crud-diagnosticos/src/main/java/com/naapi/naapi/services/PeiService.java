package com.naapi.naapi.services;

import com.naapi.naapi.dtos.PeiDTO;
import com.naapi.naapi.dtos.PeiInsertDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.PEI;
import com.naapi.naapi.entities.Usuario;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.PeiRepository;
import com.naapi.naapi.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeiService {

    private final PeiRepository repository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository; 

    @Transactional(readOnly = true)
    public List<PeiDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<PEI> list = repository.findByAlunoId(alunoId);
        return list.stream().map(PeiDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public PeiDTO insert(PeiInsertDTO dto) {
        PEI entity = new PEI();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PeiDTO(entity);
    }

    @Transactional
    public PeiDTO update(Long id, PeiInsertDTO dto) {
        PEI entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PEI não encontrado com ID: " + id));

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new PeiDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("PEI não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(PeiInsertDTO dto, PEI entity) {
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setMetas(dto.getMetas());
        entity.setEstrategias(dto.getEstrategias());
        entity.setAvaliacao(dto.getAvaliacao());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        Usuario responsavel = usuarioRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário responsável não encontrado com ID: " + dto.getResponsavelId()));

        entity.setAluno(aluno);
        entity.setResponsavel(responsavel);
    }
}