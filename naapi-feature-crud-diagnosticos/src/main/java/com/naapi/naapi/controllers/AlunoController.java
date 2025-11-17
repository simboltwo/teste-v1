package com.naapi.naapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.dtos.AlunoStatusUpdateDTO; // NOVO
import com.naapi.naapi.services.AlunoService;
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
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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


    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> insert(
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);
        AlunoDTO newDto = service.insert(dto, file); 
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> update(
            @PathVariable Long id,
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);
        AlunoDTO updatedDto = service.update(id, dto, file); 
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- NOVO ENDPOINT PATCH ---
    @PatchMapping("/{id}/status")
    public ResponseEntity<AlunoDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlunoStatusUpdateDTO dto) {
        
        AlunoDTO updatedDto = service.updateStatus(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
}