package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.LaudoDTO;
// import com.naapi.naapi.dtos.LaudoInsertDTO; // Não mais usado para insert
import com.naapi.naapi.dtos.LaudoUploadDTO; // Novo DTO
import com.naapi.naapi.services.LaudoService;

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

    /* // O Update não foi refatorado para upload de arquivo ainda.
    @PutMapping("/{id}")
    public ResponseEntity<LaudoDTO> update(@PathVariable Long id, @Valid @RequestBody LaudoInsertDTO dto) {
        LaudoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
    */
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}