package com.naapi.naapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper; // Importado
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.services.AlunoService;
import com.naapi.naapi.services.exceptions.BusinessException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType; // Importado
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Importado
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService service;
    private final ObjectMapper objectMapper; // --- NOVO ---

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> findAll(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "matricula", required = false) String matricula,
            @RequestParam(value = "cursoId", required = false) Long cursoId,
            @RequestParam(value = "turmaId", required = false) Long turmaId,
            @RequestParam(value = "diagnosticoId", required = false) Long diagnosticoId
    ) {
        List<AlunoDTO> list = service.findAll(nome, matricula, cursoId, turmaId, diagnosticoId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> findById(@PathVariable Long id) {
        AlunoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    // --- MÉTODO INSERT MODIFICADO (para aceitar Foto) ---
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> insert(
            @Valid @RequestPart("aluno") String alunoJson,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        AlunoInsertDTO dto;
        try {
            // Converte a string JSON "aluno" de volta para o DTO
            dto = objectMapper.readValue(alunoJson, AlunoInsertDTO.class);
        } catch (Exception e) {
            // Lança uma exceção de negócio se o JSON for inválido
            throw new BusinessException("Erro ao desserializar dados do aluno: " + e.getMessage());
        }

        AlunoDTO newDto = service.insert(dto, foto); // Service agora aceita o arquivo
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // --- MÉTODO UPDATE MODIFICADO (para aceitar Foto) ---
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> update(
            @PathVariable Long id,
            @Valid @RequestPart("aluno") String alunoJson,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        AlunoInsertDTO dto;
        try {
            dto = objectMapper.readValue(alunoJson, AlunoInsertDTO.class);
        } catch (Exception e) {
            throw new BusinessException("Erro ao desserializar dados do aluno: " + e.getMessage());
        }
        
        AlunoDTO updatedDto = service.update(id, dto, foto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}