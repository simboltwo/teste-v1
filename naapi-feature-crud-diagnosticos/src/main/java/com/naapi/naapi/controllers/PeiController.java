package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.PeiDTO;
import com.naapi.naapi.dtos.PeiInsertDTO;
import com.naapi.naapi.services.PeiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/peis")
@RequiredArgsConstructor
public class PeiController {

    private final PeiService service;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<PeiDTO>> findByAlunoId(@PathVariable Long alunoId) {
        List<PeiDTO> list = service.findByAlunoId(alunoId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<PeiDTO> insert(@Valid @RequestBody PeiInsertDTO dto) {
        PeiDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeiDTO> update(@PathVariable Long id, @Valid @RequestBody PeiInsertDTO dto) {
        PeiDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}