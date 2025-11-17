package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.UsuarioDTO;
import com.naapi.naapi.dtos.UsuarioInsertDTO;
import com.naapi.naapi.dtos.UsuarioUpdateDTO;
import com.naapi.naapi.dtos.UsuarioPasswordUpdateDTO; // NOVO
import com.naapi.naapi.dtos.UsuarioSelfUpdateDTO; // NOVO
import com.naapi.naapi.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    // --- ENDPOINTS DE AUTOGESTÃO (/me) ---

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> getSelf() {
        UsuarioDTO dto = service.getSelf();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me/detalhes")
    public ResponseEntity<UsuarioDTO> updateSelfDetails(@Valid @RequestBody UsuarioSelfUpdateDTO dto) {
        UsuarioDTO updatedDto = service.updateSelfDetails(dto);
        return ResponseEntity.ok(updatedDto);
    }

    @PutMapping("/me/senha")
    public ResponseEntity<Void> updateSelfPassword(@Valid @RequestBody UsuarioPasswordUpdateDTO dto) {
        service.updateSelfPassword(dto);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINTS DE ADMINISTRAÇÃO (CRUD) ---

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<UsuarioDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        UsuarioDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> insert(@Valid @RequestBody UsuarioInsertDTO dto) {
        UsuarioDTO newDto = service.insert(dto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}