package com.naapi.naapi.services;

import com.naapi.naapi.dtos.HistoricoAcademicoDTO;
import com.naapi.naapi.dtos.HistoricoAcademicoInsertDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Curso;
import com.naapi.naapi.entities.HistoricoAcademico;
import com.naapi.naapi.entities.Turma;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.CursoRepository;
import com.naapi.naapi.repositories.HistoricoAcademicoRepository;
import com.naapi.naapi.repositories.TurmaRepository;
import com.naapi.naapi.services.exceptions.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoricoAcademicoService {

    private final HistoricoAcademicoRepository repository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;

    @Transactional(readOnly = true)
    public List<HistoricoAcademicoDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }

        List<HistoricoAcademico> list = repository.findByAlunoIdOrderByDataInicioAsc(alunoId);
        return list.stream().map(HistoricoAcademicoDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public HistoricoAcademicoDTO insert(HistoricoAcademicoInsertDTO dto) {
        validateDateRange(dto.getDataInicio(), dto.getDataFim());
        HistoricoAcademico entity = new HistoricoAcademico();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new HistoricoAcademicoDTO(entity);
    }

    @Transactional
    public HistoricoAcademicoDTO update(Long id, HistoricoAcademicoInsertDTO dto) {
        HistoricoAcademico entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de Histórico Acadêmico não encontrado com ID: " + id));

        validateDateRange(dto.getDataInicio(), dto.getDataFim());
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new HistoricoAcademicoDTO(entity);
    }
    
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Registro de Histórico Acadêmico não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void copyDtoToEntity(HistoricoAcademicoInsertDTO dto, HistoricoAcademico entity) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + dto.getCursoId()));
        
        Turma turma = null;
        if (dto.getTurmaId() != null) {
            turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + dto.getTurmaId()));
        }

        entity.setAluno(aluno);
        entity.setCurso(curso);
        entity.setTurma(turma);
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
    }
    
    private void validateDateRange(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessException("A Data de Início não pode ser posterior à Data de Fim.");
        }
    }
}