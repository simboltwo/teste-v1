package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.AtendimentoDTO;
import com.naapi.naapi.dtos.AtendimentoInsertDTO;
import com.naapi.naapi.services.AtendimentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService service;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AtendimentoDTO>> findByAlunoId(@PathVariable Long alunoId) {
        List<AtendimentoDTO> list = service.findByAlunoId(alunoId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<AtendimentoDTO> insert(@Valid @RequestBody AtendimentoInsertDTO dto) {
        AtendimentoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoDTO> update(@PathVariable Long id, @Valid @RequestBody AtendimentoInsertDTO dto) {
        AtendimentoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}