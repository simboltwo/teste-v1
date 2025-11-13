// src/main/java/com/naapi/naapi/services/AlunoService.java
package com.naapi.naapi.services;

// --- IMPORTAÇÕES NOVAS ---
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
// --- FIM DAS IMPORTAÇÕES NOVAS ---
import java.util.stream.Collectors;

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
import com.naapi.naapi.services.specifications.AlunoSpecifications;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;// ... (outros imports)
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
// ... (outros imports)

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    
    // --- INJETAR O CLOUDINARY ---
    private final Cloudinary cloudinary; 

    // ... (o método findAll permanece igual) ...
    @Transactional(readOnly = true)
    public List<AlunoDTO> findAll(
            String nome, 
            String matricula, 
            List<Long> cursoIds, 
            Long turmaId, 
            List<Long> diagnosticoIds
    ) {
        // ... (lógica do spec)
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

        List<Aluno> list = repository.findAll(spec);
        return list.stream().map(AlunoDTO::new).collect(Collectors.toList());
    }


    // ... (o método findById permanece igual) ...
    @Transactional(readOnly = true)
    public AlunoDTO findById(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        return new AlunoDTO(entity);
    }


    // --- MÉTODO INSERT ATUALIZADO ---
    @Transactional
    public AlunoDTO insert(AlunoInsertDTO dto, MultipartFile file) throws IOException {
        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), -1L)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }
        
        Aluno entity = new Aluno();
        
        // --- LÓGICA DE UPLOAD ---
        if (file != null && !file.isEmpty()) {
            // 1. Envia o ficheiro para o Cloudinary na pasta "naapi_fotos"
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "naapi_fotos"));
            
            // 2. Obtém a URL segura (https://)
            String fotoUrl = (String) uploadResult.get("secure_url");
            
            // 3. Define a URL no DTO antes de salvar
            dto.setFoto(fotoUrl);
        }
        // --- FIM DA LÓGICA DE UPLOAD ---

        copyDtoToEntity(dto, entity);
        entity.setAtivo(true);
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    // --- MÉTODO UPDATE ATUALIZADO ---
    @Transactional
    public AlunoDTO update(Long id, AlunoInsertDTO dto, MultipartFile file) throws IOException {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), id)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }

        // --- LÓGICA DE UPLOAD (COM BÓNUS DE APAGAR A FOTO ANTIGA) ---
        String oldFotoUrl = entity.getFoto(); // Guarda a URL da foto antiga

        if (file != null && !file.isEmpty()) {
            // 1. Envia o ficheiro NOVO
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap("folder", "naapi_fotos"));
            
            String novaFotoUrl = (String) uploadResult.get("secure_url");
            dto.setFoto(novaFotoUrl); // Define a NOVA foto no DTO

            // --- INÍCIO DA MUDANÇA ---
            // 2. Apaga a foto ANTIGA do Cloudinary (se existir)
            if (oldFotoUrl != null && !oldFotoUrl.isBlank()) {
                try {
                    // Extrai o "public_id" da URL antiga para saber qual ficheiro apagar
                    String publicId = extractPublicIdFromUrl(oldFotoUrl);
                    if (publicId != null && !publicId.isBlank()) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao apagar foto antiga do Cloudinary: " + e.getMessage());
                }
            }
        }
        // --- FIM DA LÓGICA DE UPLOAD ---

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    // ... (o método delete permanece igual) ...
    @Transactional
    public void delete(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        
        // NOTA: No futuro, podes querer apagar a foto do Cloudinary aqui também
        
        entity.setAtivo(false);
        repository.save(entity);
    }

    // ... (o método copyDtoToEntity permanece igual) ...
    private void copyDtoToEntity(AlunoInsertDTO dto, Aluno entity) {
        entity.setNome(dto.getNome());
        // ... (resto do método igual)
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
            // 1. Garante que é uma URL do Cloudinary
            if (url == null || !url.contains("res.cloudinary.com")) {
                return null; // Não é uma URL do Cloudinary, não tenta apagar
            }
            
            // 2. Encontra a parte relevante depois de "/image/upload/"
            String relevantPart = url.split("/image/upload/")[1];
            
            // 3. Remove a versão (ex: "v12345/") se existir
            relevantPart = relevantPart.replaceAll("v\\d+/", "");
            
            // 4. Remove a extensão do ficheiro (ex: ".png", ".jpg")
            String publicId = relevantPart.substring(0, relevantPart.lastIndexOf('.'));
            return publicId;

        } catch (Exception e) {
            System.err.println("Não foi possível extrair o public_id da URL: " + url);
            return null; // Não foi possível extrair
        }
    }
}