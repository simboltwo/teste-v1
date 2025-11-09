package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.*;
import com.naapi.naapi.services.CursoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {
    
    private final CursoService service;
    @GetMapping
    public ResponseEntity<List<CursoDTO>> findAll() {
        List<CursoDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> findById(@PathVariable Long id) {
        CursoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CursoDTO> insert(@Valid @RequestBody CursoDTO dto) {
        CursoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> update(@PathVariable Long id, @Valid @RequestBody CursoDTO dto) {
        CursoDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}