// src/main/java/com/naapi/naapi/controllers/AlunoController.java
package com.naapi.naapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// ... (outros imports)
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.services.AlunoService;
// REMOVE O FileUploadService
// import com.naapi.naapi.services.FileUploadService; 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException; 
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService service;
    // REMOVE O FileUploadService
    // private final FileUploadService fileUploadService; 
    
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // ... (o método findAll e findById permanecem iguais) ...
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> findAll(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "matricula", required = false) String matricula,
            @RequestParam(value = "cursoId", required = false) List<Long> cursoIds, 
            @RequestParam(value = "turmaId", required = false) Long turmaId,
            @RequestParam(value = "diagnosticoId", required = false) List<Long> diagnosticoIds 
    ) {
        List<AlunoDTO> list = service.findAll(nome, matricula, cursoIds, turmaId, diagnosticoIds);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> findById(@PathVariable Long id) {
        AlunoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }


    // --- MÉTODO INSERT ATUALIZADO ---
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> insert(
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);

        // MUDANÇA: Passa o ficheiro para o AlunoService
        AlunoDTO newDto = service.insert(dto, file); 
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // --- MÉTODO UPDATE ATUALIZADO ---
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> update(
            @PathVariable Long id,
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);

        // MUDANÇA: Passa o ficheiro para o AlunoService
        AlunoDTO updatedDto = service.update(id, dto, file); 
        return ResponseEntity.ok(updatedDto);
    }

    // ... (o método delete permanece igual) ...
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}