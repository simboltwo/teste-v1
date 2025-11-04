// src/main/java/com/naapi/naapi/services/LaudoService.java
package com.naapi.naapi.services;

import com.naapi.naapi.dtos.LaudoDTO;
// import com.naapi.naapi.dtos.LaudoInsertDTO; // Substituído
import com.naapi.naapi.dtos.LaudoUploadDTO; // Novo DTO
import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Laudo;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.LaudoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // Novo import

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaudoService {

    private final LaudoRepository repository;
    private final AlunoRepository alunoRepository;
    private final FileStorageService fileStorageService; // --- NOVO: Injetar serviço de arquivo ---

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
        // ... (lógica existente)
        if (!alunoRepository.existsById(alunoId)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + alunoId);
        }
        List<Laudo> list = repository.findByAlunoId(alunoId);
        return list.stream().map(LaudoDTO::new).collect(Collectors.toList());
    }

    // --- MÉTODO INSERT MODIFICADO ---
    @Transactional
    public LaudoDTO insert(LaudoUploadDTO dto, MultipartFile file) {
        // 1. Salvar o arquivo PDF e obter a URL
        String urlArquivo = fileStorageService.saveFile(file, "laudos");

        // 2. Criar a entidade Laudo
        Laudo entity = new Laudo();
        entity.setDataEmissao(dto.getDataEmissao());
        entity.setDescricao(dto.getDescricao());
        entity.setUrlArquivo(urlArquivo); // Salva a URL retornada

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));
        entity.setAluno(aluno);
        
        // 3. Salvar no banco
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }
    
    // O service.update() precisaria ser refatorado se o usuário puder trocar o arquivo
    // Por enquanto, vamos remover o método 'update' antigo, já que o DTO mudou
    /*
    @Transactional
    public LaudoDTO update(Long id, LaudoInsertDTO dto) {
        // ...
    }
    */

    @Transactional
    public void delete(Long id) {
        // TODO: Adicionar lógica para deletar o arquivo do storage
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Laudo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    // O método 'copyDtoToEntity' não é mais usado da mesma forma, pois os dados vêm de locais diferentes
}