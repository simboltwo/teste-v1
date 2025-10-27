package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.*;
import com.naapi.naapi.services.DiagnosticoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService service;

    @GetMapping
    public ResponseEntity<List<DiagnosticoDTO>> findAll() {
        List<DiagnosticoDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> findById(@PathVariable Long id) {
        DiagnosticoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DiagnosticoDTO> insert(@Valid @RequestBody DiagnosticoDTO dto) {
        DiagnosticoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoDTO> update(@PathVariable Long id, @Valid @RequestBody DiagnosticoDTO dto) {
        DiagnosticoDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
