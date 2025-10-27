package com.naapi.naapi.services;

import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Curso;
import com.naapi.naapi.entities.Diagnostico;
import com.naapi.naapi.entities.Turma;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.CursoRepository;
import com.naapi.naapi.repositories.DiagnosticoRepository;
import com.naapi.naapi.repositories.TurmaRepository;
import com.naapi.naapi.services.exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;

    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final DiagnosticoRepository diagnosticoRepository;

    @Transactional(readOnly = true)
    public List<AlunoDTO> findAll() {
        List<Aluno> list = repository.findAll();
        return list.stream().map(AlunoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoDTO findById(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        return new AlunoDTO(entity);
    }

    @Transactional
    public AlunoDTO insert(AlunoInsertDTO dto) {
        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), -1L)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }
        
        Aluno entity = new Aluno();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    @Transactional
    public AlunoDTO update(Long id, AlunoInsertDTO dto) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), id)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(AlunoInsertDTO dto, Aluno entity) {
        // Copia os dados simples
        entity.setNome(dto.getNome());
        entity.setNomeSocial(dto.getNomeSocial());
        entity.setMatricula(dto.getMatricula());
        entity.setFoto(dto.getFoto());
        entity.setPrioridadeAtendimento(dto.getPrioridadeAtendimento());

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + dto.getCursoId()));
        entity.setCurso(curso);

        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + dto.getTurmaId()));
        entity.setTurma(turma);

        entity.getDiagnosticos().clear();
        if (dto.getDiagnosticosId() != null) {
            for (Long diagId : dto.getDiagnosticosId()) {
                Diagnostico diag = diagnosticoRepository.findById(diagId)
                        .orElseThrow(() -> new EntityNotFoundException("Diagnóstico não encontrado com ID: " + diagId));
                entity.getDiagnosticos().add(diag);
            }
        }
    }
}