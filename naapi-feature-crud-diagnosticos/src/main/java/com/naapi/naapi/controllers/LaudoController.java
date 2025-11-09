package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.LaudoDTO;
import com.naapi.naapi.dtos.LaudoInsertDTO;
import com.naapi.naapi.services.LaudoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<LaudoDTO> insert(@Valid @RequestBody LaudoInsertDTO dto) {
        LaudoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaudoDTO> update(@PathVariable Long id, @Valid @RequestBody LaudoInsertDTO dto) {
        LaudoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}