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
    private final FileStorageService fileStorageService; // --- NOVO ---

    @Transactional(readOnly = true)
    public List<LaudoDTO> findByAlunoId(Long alunoId) {
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

    /*
    // O método update antigo usava LaudoInsertDTO, que foi depreciado
    @Transactional
    public LaudoDTO update(Long id, LaudoInsertDTO dto) {
        Laudo entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado com ID: " + id));

        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new LaudoDTO(entity);
    }
    */

    @Transactional
    public void delete(Long id) {
        // TODO: Adicionar lógica para deletar o arquivo físico do storage
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Laudo não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }

    /*
    private void copyDtoToEntity(LaudoInsertDTO dto, Laudo entity) {
        entity.setDataEmissao(dto.getDataEmissao());
        entity.setUrlArquivo(dto.getUrlArquivo());
        entity.setDescricao(dto.getDescricao());

        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + dto.getAlunoId()));

        entity.setAluno(aluno);
    }
    */
}