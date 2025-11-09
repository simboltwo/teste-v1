package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.*;
import com.naapi.naapi.services.TipoAtendimentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipos-atendimento")
@RequiredArgsConstructor
public class TipoAtendimentoController {
    
    private final TipoAtendimentoService service;

    @GetMapping
    public ResponseEntity<List<TipoAtendimentoDTO>> findAll() {
        List<TipoAtendimentoDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAtendimentoDTO> findById(@PathVariable Long id) {
        TipoAtendimentoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TipoAtendimentoDTO> insert(@Valid @RequestBody TipoAtendimentoDTO dto) {
        TipoAtendimentoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAtendimentoDTO> update(@PathVariable Long id, @Valid @RequestBody TipoAtendimentoDTO dto) {
        TipoAtendimentoDTO newDto = service.update(id, dto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}