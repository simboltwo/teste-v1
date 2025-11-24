package com.naapi.naapi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.dtos.AlunoStatusUpdateDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Curso;
import com.naapi.naapi.entities.Diagnostico;
import com.naapi.naapi.entities.Turma;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.CursoRepository;
import com.naapi.naapi.repositories.DiagnosticoRepository;
import com.naapi.naapi.repositories.TurmaRepository;
import com.naapi.naapi.services.exceptions.BusinessException;
import com.naapi.naapi.services.specifications.AlunoSpecifications;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final Cloudinary cloudinary; 
    // REMOVIDO: ApplicationEventPublisher

    
    @Transactional(readOnly = true)
    public List<AlunoDTO> findAll(
            String nome, 
            String matricula, 
            List<Long> cursoIds, 
            Long turmaId, 
            List<Long> diagnosticoIds,
            LocalDate atendimentoData, 
            String atendimentoStatus,
            List<Long> turmasLecionadasIds
    ) {
        Specification<Aluno> spec = Specification.where(null);

        if (nome != null && !nome.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasNome(nome));
        }
        if (matricula != null && !matricula.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasMatricula(matricula));
        }
        if (cursoIds != null && !cursoIds.isEmpty()) { 
            spec = spec.and(AlunoSpecifications.hasCursoIds(cursoIds)); 
        }
        if (turmaId != null) {
            spec = spec.and(AlunoSpecifications.hasTurmaId(turmaId));
        }
        if (diagnosticoIds != null && !diagnosticoIds.isEmpty()) { 
            spec = spec.and(AlunoSpecifications.hasDiagnosticoIds(diagnosticoIds));
        }
        if (atendimentoData != null && atendimentoStatus != null && !atendimentoStatus.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasAtendimentoAgendadoParaData(atendimentoData, atendimentoStatus));
        }
        if (turmasLecionadasIds != null && !turmasLecionadasIds.isEmpty()) {
            spec = spec.and(AlunoSpecifications.hasTurmaIds(turmasLecionadasIds));
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
    public AlunoDTO insert(AlunoInsertDTO dto, MultipartFile file) throws IOException {
        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), -1L)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }
        
        Aluno entity = new Aluno();
        
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "naapi_fotos"));
            String fotoUrl = (String) uploadResult.get("secure_url");
            dto.setFoto(fotoUrl);
        }
        
        copyDtoToEntity(dto, entity);
        entity.setAtivo(true);
        entity = repository.save(entity);
        
        AlunoDTO newDto = new AlunoDTO(entity);

        // REMOVIDO: eventPublisher.publishEvent(...)

        return newDto;
    }

    @Transactional
    public AlunoDTO update(Long id, AlunoInsertDTO dto, MultipartFile file) throws IOException {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), id)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }

        String oldFotoUrl = entity.getFoto(); 

        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "naapi_fotos"));
            
            String novaFotoUrl = (String) uploadResult.get("secure_url");
            dto.setFoto(novaFotoUrl); 

            if (oldFotoUrl != null && !oldFotoUrl.isBlank()) {
                try {
                    String publicId = extractPublicIdFromUrl(oldFotoUrl);
                    if (publicId != null && !publicId.isBlank()) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao apagar foto antiga do Cloudinary: " + e.getMessage());
                }
            }
        }
        
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        
        // TODO: Apagar a foto do Cloudinary aqui também
        
        entity.setAtivo(false);
        repository.save(entity);
    }

    // --- NOVO MÉTODO PARA ATUALIZAÇÃO PARCIAL (PATCH) ---
    @Transactional
    public AlunoDTO updateStatus(Long id, AlunoStatusUpdateDTO dto) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        // Atualiza os campos apenas se eles não forem nulos no DTO
        if (dto.getPrioridade() != null) {
            entity.setPrioridade(dto.getPrioridade());
        }
        
        if (dto.getProvaOutroEspaco() != null) {
            entity.setProvaOutroEspaco(dto.getProvaOutroEspaco());
        }

        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    private void copyDtoToEntity(AlunoInsertDTO dto, Aluno entity) {
        entity.setNome(dto.getNome());
        entity.setNomeSocial(dto.getNomeSocial());
        entity.setMatricula(dto.getMatricula());
        
        if (dto.getFoto() != null) { 
            entity.setFoto(dto.getFoto());
        }
        
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

    private String extractPublicIdFromUrl(String url) {
        try {
            if (url == null || !url.contains("res.cloudinary.com")) {
                return null; 
            }
            
            String relevantPart = url.split("/image/upload/")[1];
            relevantPart = relevantPart.replaceAll("v\\d+/", "");
            String publicId = relevantPart.substring(0, relevantPart.lastIndexOf('.'));
            return publicId;

        } catch (Exception e) {
            System.err.println("Não foi possível extrair o public_id da URL: " + url);
            return null;
        }
    }
}