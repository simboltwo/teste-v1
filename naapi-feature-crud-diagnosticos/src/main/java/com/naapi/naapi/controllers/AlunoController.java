package com.naapi.naapi.controllers;

// Removidos: ObjectMapper, Validator, ConstraintViolation, Set, Collectors
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.services.AlunoService;
import com.naapi.naapi.services.exceptions.BusinessException; // Pode ser necessário para o service

import jakarta.validation.Valid; // Importado
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService service;
    // Removidos: ObjectMapper e Validator (não são mais necessários aqui)

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

    // --- MÉTODO INSERT MODIFICADO (para aceitar DTO e validar) ---
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> insert(
            // 1. Mude de String para AlunoInsertDTO
            // 2. Adicione @Valid para acionar a validação automática
            @RequestPart("aluno") @Valid AlunoInsertDTO dto, 
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        // 3. Remova toda a lógica de desserialização e validação manual
        
        AlunoDTO newDto = service.insert(dto, foto); 
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // --- MÉTODO UPDATE MODIFICADO (para aceitar DTO e validar) ---
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> update(
            @PathVariable Long id,
            // 1. Mude de String para AlunoInsertDTO
            // 2. Adicione @Valid para acionar a validação automática
            @RequestPart("aluno") @Valid AlunoInsertDTO dto,
            @RequestPart(value = "foto", required = false) MultipartFile foto
    ) {
        // 3. Remova toda a lógica de desserialização e validação manual
        
        AlunoDTO updatedDto = service.update(id, dto, foto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}