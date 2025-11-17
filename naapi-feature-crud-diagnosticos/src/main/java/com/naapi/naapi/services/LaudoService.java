package com.naapi.naapi.services;

// --- NOVOS IMPORTS ---
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
import org.springframework.web.multipart.MultipartFile; // Importar

import java.io.IOException; // Importar
import java.util.List;
import java.util.Map; // Importar
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final AlunoRepository alunoRepository;
    
    // --- INJETAR CLOUDINARY ---
    private final Cloudinary cloudinary;

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<Laudo> list = repository.findByAlunoId(alunoId);
        return list.stream().map(LaudoDTO::new).collect(Collectors.toList());
    }

    // --- MÉTODO INSERT ATUALIZADO ---
    @Transactional
    public LaudoDTO insert(LaudoInsertDTO dto, MultipartFile file) throws IOException {
        Laudo entity = new Laudo();
        
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo do laudo (PDF) é obrigatório.");
        }

        // 1. Envia o ficheiro para o Cloudinary
        // "resource_type", "auto" permite o upload de PDFs, vídeos, etc.
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
            ObjectUtils.asMap(
                "folder", "naapi_laudos",
                "resource_type", "auto" 
            ));
        
        // 2. Obtém a URL segura
        String fileUrl = (String) uploadResult.get("secure_url");
        entity.setUrlArquivo(fileUrl); // Define a URL na entidade

        // 3. Copia o resto do DTO
        copyDtoToEntity(dto, entity);
        
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    // --- MÉTODO UPDATE ATUALIZADO ---
    @Transactional
    public LaudoDTO update(Long id, LaudoInsertDTO dto, MultipartFile file) throws IOException {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        String oldFileUrl = entity.getUrlArquivo(); // Guarda a URL antiga

        // Se um novo ficheiro foi enviado, faz o upload e apaga o antigo
        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "naapi_laudos",
                    "resource_type", "auto"
                ));
            
            String newFileUrl = (String) uploadResult.get("secure_url");
            entity.setUrlArquivo(newFileUrl); // Define a NOVA URL

            // Tenta apagar o ficheiro antigo do Cloudinary
            if (oldFileUrl != null && !oldFileUrl.isBlank()) {
                try {
                    String publicId = extractPublicIdFromUrl(oldFileUrl);
                    if (publicId != null) {
                        // "resource_type" "raw" ou "auto" é necessário se não for imagem
                        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao apagar laudo antigo do Cloudinary: " + e.getMessage());
                    // Não para a execução, apenas loga o erro
                }
            }
        }

        copyDtoToEntity(dto, entity); // Atualiza os outros dados (data, descrição)
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        // --- LÓGICA PARA APAGAR DO CLOUDINARY ---
        String fileUrl = entity.getUrlArquivo();
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                String publicId = extractPublicIdFromUrl(fileUrl);
                 if (publicId != null) {
                    // "resource_type" "raw" ou "auto" é necessário se não for imagem
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
        // entity.setUrlArquivo(dto.getUrlArquivo()); // REMOVIDO DAQUI
        entity.setDescricao(dto.getDescricao());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        entity.setAluno(aluno);
    }
    
    // --- MÉTODO HELPER (COPIADO DO ALUNOSERVICE) ---
    private String extractPublicIdFromUrl(String url) {
        try {
            if (url == null || !url.contains("res.cloudinary.com")) {
                return null;
            }
            
            // Lida com /image/upload/, /video/upload/ ou /raw/upload/
            String relevantPart = url.split("/upload/")[1];
            
            relevantPart = relevantPart.replaceAll("v\\d+/", "");
            
            // Para "raw", a extensão pode não estar presente na URL, 
            // mas o public_id inclui a pasta (naapi_laudos/)
            if(url.contains("/raw/upload/")) {
                return relevantPart; // ex: "naapi_laudos/arquivo.pdf"
            }
            
            // Para imagens/videos, remove a extensão
            return relevantPart.substring(0, relevantPart.lastIndexOf('.'));

        } catch (Exception e) {
            System.err.println("Não foi possível extrair o public_id da URL: " + url);
            return null;
        }
    }
}