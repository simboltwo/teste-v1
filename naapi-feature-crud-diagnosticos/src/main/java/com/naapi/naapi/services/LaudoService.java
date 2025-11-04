package com.naapi.naapi.services;

import com.naapi.naapi.dtos.LaudoDTO;
import com.naapi.naapi.dtos.LaudoUploadDTO;
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Laudo;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.LaudoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final AlunoRepository alunoRepository;
    
    // --- DEPENDÊNCIA MODIFICADA ---
    private final CloudStorageService cloudStorageService; // Era FileStorageService

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        
        List<Laudo> list = repository.findByAlunoId(alunoId);
        return list.stream().map(LaudoDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public LaudoDTO insert(LaudoUploadDTO dto, MultipartFile file) {
        
        // --- LÓGICA DE UPLOAD MODIFICADA ---
        // 1. Salvar o arquivo PDF no Cloudinary
        String urlArquivo = cloudStorageService.uploadFile(file, "naapi/laudos");

        // 2. Criar a entidade Laudo
        Laudo entity = new Laudo();
        entity.setDataEmissao(dto.getDataEmissao());
        entity.setDescricao(dto.getDescricao());
        entity.setUrlArquivo(urlArquivo); // Salva a URL retornada pelo Cloudinary

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));
        entity.setAluno(aluno);
        
        // 3. Salvar no banco
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        // TODO: Adicionar lógica para deletar o arquivo do Cloudinary
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Laudo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}