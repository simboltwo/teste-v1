/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/services/LaudoService.java
 * Descrição: Adicionado "flags", "inline" nas opções de upload do Cloudinary.
 */
package com.naapi.naapi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.web.multipart.MultipartFile; 

import java.io.IOException; 
import java.util.List;
import java.util.Map; 
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final AlunoRepository alunoRepository;
    private final Cloudinary cloudinary;

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<Laudo> list = repository.findByAlunoId(alunoId);
        return list.stream().map(LaudoDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public LaudoDTO insert(LaudoInsertDTO dto, MultipartFile file) throws IOException {
        Laudo entity = new Laudo();
        
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo do laudo (PDF) é obrigatório.");
        }

        // --- INÍCIO DA MUDANÇA ---
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "folder", "naapi_laudos",
                "resource_type", "auto",
                "flags", "inline" // Força o Content-Disposition: inline
            ));
        // --- FIM DA MUDANÇA ---
        
        String fileUrl = (String) uploadResult.get("secure_url");
        entity.setUrlArquivo(fileUrl); 

        copyDtoToEntity(dto, entity);
        
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public LaudoDTO update(Long id, LaudoInsertDTO dto, MultipartFile file) throws IOException {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        String oldFileUrl = entity.getUrlArquivo(); 

        if (file != null && !file.isEmpty()) {
            // --- INÍCIO DA MUDANÇA ---
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "naapi_laudos",
                    "resource_type", "auto",
                    "flags", "inline" // Força o Content-Disposition: inline
                ));
            // --- FIM DA MUDANÇA ---
            
            String newFileUrl = (String) uploadResult.get("secure_url");
            entity.setUrlArquivo(newFileUrl); 

            if (oldFileUrl != null && !oldFileUrl.isBlank()) {
                try {
                    String publicId = extractPublicIdFromUrl(oldFileUrl, true); // true para raw/auto
                    if (publicId != null) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao apagar laudo antigo do Cloudinary: " + e.getMessage());
                }
            }
        }

        copyDtoToEntity(dto, entity); 
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        String fileUrl = entity.getUrlArquivo();
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                String publicId = extractPublicIdFromUrl(fileUrl, true); // true para raw/auto
                 if (publicId != null) {
                    cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
                 }
            } catch (Exception e) {
                 System.err.println("Erro ao apagar laudo do Cloudinary na exclusão: " + e.getMessage());
            }
        }
        
        repository.deleteById(id);
    }

    private void copyDtoToEntity(LaudoInsertDTO dto, Laudo entity) {
        entity.setDataEmissao(dto.getDataEmissao());
        entity.setDescricao(dto.getDescricao());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        entity.setAluno(aluno);
    }
    
    private String extractPublicIdFromUrl(String url, boolean isRaw) {
        try {
            if (url == null || !url.contains("res.cloudinary.com")) {
                return null;
            }
            
            String relevantPart = url.split("/upload/")[1];
            relevantPart = relevantPart.replaceAll("v\\d+/", "");
            
            if(isRaw) {
                return relevantPart; 
            }
            
            return relevantPart.substring(0, relevantPart.lastIndexOf('.'));

        } catch (Exception e) {
            System.err.println("Não foi possível extrair o public_id da URL: " + url);
            return null;
        }
    }
}