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
import com.naapi.naapi.services.specifications.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

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
    public List<AlunoDTO> findAll(
            String nome, 
            String matricula, 
            // MUDANÇA 1: Aceitar List<Long>
            List<Long> cursoIds, 
            Long turmaId, 
            // MUDANÇA 2: Aceitar List<Long>
            List<Long> diagnosticoIds
    ) {
        
        Specification<Aluno> spec = Specification.where(null);

        if (nome != null && !nome.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasNome(nome));
        }
        if (matricula != null && !matricula.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasMatricula(matricula));
        }
        
        // MUDANÇA 3: Verificar se a lista não está vazia
        if (cursoIds != null && !cursoIds.isEmpty()) { 
            // MUDANÇA 4: Chamar a nova especificação (hasCursoIds no plural)
            spec = spec.and(AlunoSpecifications.hasCursoIds(cursoIds)); 
        }

        if (turmaId != null) {
            spec = spec.and(AlunoSpecifications.hasTurmaId(turmaId));
        }
        
        // MUDANÇA 5: Verificar se a lista não está vazia
        if (diagnosticoIds != null && !diagnosticoIds.isEmpty()) { 
            // MUDANÇA 6: Chamar a nova especificação (hasDiagnosticoIds no plural)
            spec = spec.and(AlunoSpecifications.hasDiagnosticoIds(diagnosticoIds));
        }

        List<Aluno> list = repository.findAll(spec);
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
        entity.setAtivo(true);
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
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        
        entity.setAtivo(false);
        repository.save(entity);
    }

    // --- MÉTODO ATUALIZADO ---
    private void copyDtoToEntity(AlunoInsertDTO dto, Aluno entity) {
        entity.setNome(dto.getNome());
        entity.setNomeSocial(dto.getNomeSocial());
        entity.setMatricula(dto.getMatricula());
        
        // A foto é definida pelo serviço de upload antes de chamar este método
        if (dto.getFoto() != null) { 
            entity.setFoto(dto.getFoto());
        }
        
        // Todos os campos atualizados
        entity.setPrioridade(dto.getPrioridade());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setCpf(dto.getCpf());
        entity.setTelefoneEstudante(dto.getTelefoneEstudante());
        entity.setProvaOutroEspaco(dto.getProvaOutroEspaco());
        entity.setProcessoSipac(dto.getProcessoSipac());
        entity.setAnotacoesNaapi(dto.getAnotacoesNaapi());
        entity.setAdaptacoesNecessarias(dto.getAdaptacoesNecessarias());
        entity.setNecessidadesRelatoriosMedicos(dto.getNecessidadesRelatoriosMedicos());

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: ".concat(dto.getCursoId().toString())));
        entity.setCurso(curso);

        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: ".concat(dto.getTurmaId().toString())));
        entity.setTurma(turma);

        entity.getDiagnosticos().clear();
        if (dto.getDiagnosticosId() != null) {
            for (Long diagId : dto.getDiagnosticosId()) {
                Diagnostico diag = diagnosticoRepository.findById(diagId)
                        .orElseThrow(() -> new EntityNotFoundException("Diagnóstico não encontrado com ID: ".concat(diagId.toString())));
                entity.getDiagnosticos().add(diag);
            }
        }
    }
}