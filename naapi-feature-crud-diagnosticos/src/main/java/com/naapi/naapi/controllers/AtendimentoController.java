/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/controllers/AtendimentoController.java
 * Descrição: Modificado GET /aluno/{id}, adicionado GET /me e GET /{id}.
 */
package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.AtendimentoDTO;
import com.naapi.naapi.dtos.AtendimentoInsertDTO;
import com.naapi.naapi.dtos.AtendimentoStatusUpdateDTO;
import com.naapi.naapi.entities.Usuario; // Importar
import com.naapi.naapi.services.AtendimentoService;
import com.naapi.naapi.services.UsuarioService; // Importar

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
    private final UsuarioService usuarioService; // Injetar UsuarioService

    // --- INÍCIO DA MUDANÇA ---
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoDTO> findById(@PathVariable Long id) {
        AtendimentoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AtendimentoDTO>> findByAlunoId(
            @PathVariable Long alunoId,
            @RequestParam(value = "status", required = false) String status) {
        List<AtendimentoDTO> list = service.findByAlunoId(alunoId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/me")
    public ResponseEntity<List<AtendimentoDTO>> findByResponsavel() {
        // Pega o usuário logado
        Usuario usuarioLogado = usuarioService.getAuthenticatedUser();
        List<AtendimentoDTO> list = service.findByResponsavelId(usuarioLogado.getId());
        return ResponseEntity.ok(list);
    }
    // --- FIM DA MUDANÇA ---

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<AtendimentoDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtendimentoStatusUpdateDTO dto) {
        
        AtendimentoDTO updatedDto = service.updateStatus(id, dto);
        return ResponseEntity.ok(updatedDto);
    }
}