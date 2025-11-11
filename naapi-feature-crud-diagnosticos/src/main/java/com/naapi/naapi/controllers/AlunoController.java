package com.naapi.naapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.services.AlunoService;
import com.naapi.naapi.services.FileUploadService; // Importar
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType; // Importar
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Importar
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException; // Importar
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService service;
    private final FileUploadService fileUploadService; // Injetar o serviço de upload
    
    // Configura o ObjectMapper para entender datas como LocalDate
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // --- (findAll e findById permanecem iguais) ---
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> findAll(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "matricula", required = false) String matricula,
            // MUDANÇA 1: Mudar de Long para List<Long>
            @RequestParam(value = "cursoId", required = false) List<Long> cursoIds, 
            @RequestParam(value = "turmaId", required = false) Long turmaId,
            // MUDANÇA 2: Mudar de Long para List<Long>
            @RequestParam(value = "diagnosticoId", required = false) List<Long> diagnosticoIds 
    ) {
        // MUDANÇA 3: Passar as variáveis corretas (plural) para o serviço
        List<AlunoDTO> list = service.findAll(nome, matricula, cursoIds, turmaId, diagnosticoIds);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> findById(@PathVariable Long id) {
        AlunoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    // --- MÉTODO INSERT ATUALIZADO (Corrige o 415) ---
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> insert(
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        // 1. Converte a string JSON de volta para o DTO
        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);

        // 2. Salva o ficheiro (se existir) e define a URL da foto no DTO
        if (file != null && !file.isEmpty()) {
            String fotoUrl = fileUploadService.saveFile(file);
            dto.setFoto(fotoUrl);
        }
        
        // 3. Salva o aluno
        AlunoDTO newDto = service.insert(dto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // --- MÉTODO UPDATE ATUALIZADO (Corrige o 415) ---
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AlunoDTO> update(
            @PathVariable Long id,
            @RequestPart("alunoDTO") String alunoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        // 1. Converte a string JSON de volta para o DTO
        AlunoInsertDTO dto = objectMapper.readValue(alunoDtoString, AlunoInsertDTO.class);

        // 2. Salva o ficheiro (se existir) e define a URL da foto no DTO
        if (file != null && !file.isEmpty()) {
            String fotoUrl = fileUploadService.saveFile(file);
            dto.setFoto(fotoUrl);
        }

        // 3. Atualiza o aluno
        AlunoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    // --- (delete permanece igual) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}