// src/main/java/com/naapi/naapi/controllers/LaudoController.java
package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.LaudoDTO;
// import com.naapi.naapi.dtos.LaudoInsertDTO; // Substituído
import com.naapi.naapi.dtos.LaudoUploadDTO; // Novo DTO
import com.naapi.naapi.services.LaudoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/laudos")
@RequiredArgsConstructor
public class LaudoController {

    private final LaudoService service;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<LaudoDTO>> findByAlunoId(@PathVariable Long alunoId) {
        List<LaudoDTO> list = service.findByAlunoId(alunoId);
        return ResponseEntity.ok(list);
    }

    // --- MÉTODO INSERT TOTALMENTE MODIFICADO ---
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<LaudoDTO> insert(
            @Valid @ModelAttribute LaudoUploadDTO dto,
            @RequestParam("file") MultipartFile file
    ) {
        // O service agora recebe o DTO de dados e o arquivo
        LaudoDTO newDto = service.insert(dto, file);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    // O Update não foi solicitado para upload, então o mantemos
    // Se precisar que o Update também aceite arquivo, ele precisará de refatoração similar
    /*
    @PutMapping("/{id}")
    public ResponseEntity<LaudoDTO> update(@PathVariable Long id, @Valid @RequestBody LaudoInsertDTO dto) {
        LaudoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
    */
    
    // Delete continua igual
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}