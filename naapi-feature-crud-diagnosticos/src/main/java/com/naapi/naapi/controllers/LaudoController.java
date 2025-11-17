package com.naapi.naapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.naapi.naapi.dtos.LaudoDTO;
import com.naapi.naapi.dtos.LaudoInsertDTO;
import com.naapi.naapi.services.LaudoService;

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
@RequestMapping("/laudos")
@RequiredArgsConstructor
public class LaudoController {

    private final LaudoService service;
    
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<LaudoDTO>> findByAlunoId(@PathVariable Long alunoId) {
        List<LaudoDTO> list = service.findByAlunoId(alunoId);
        return ResponseEntity.ok(list);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<LaudoDTO> insert(
            @RequestPart("laudoDTO") String laudoDtoString,
            @RequestPart("file") MultipartFile file) throws IOException {
        
        LaudoInsertDTO dto = objectMapper.readValue(laudoDtoString, LaudoInsertDTO.class);
        LaudoDTO newDto = service.insert(dto, file);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<LaudoDTO> update(
            @PathVariable Long id,
            @RequestPart("laudoDTO") String laudoDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        
        LaudoInsertDTO dto = objectMapper.readValue(laudoDtoString, LaudoInsertDTO.class);
        LaudoDTO updatedDto = service.update(id, dto, file);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}