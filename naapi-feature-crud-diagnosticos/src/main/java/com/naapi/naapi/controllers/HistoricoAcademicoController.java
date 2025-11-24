package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.HistoricoAcademicoDTO;
import com.naapi.naapi.dtos.HistoricoAcademicoInsertDTO;
import com.naapi.naapi.services.HistoricoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/historico-academico")
@RequiredArgsConstructor
public class HistoricoAcademicoController {

    private final HistoricoAcademicoService service;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<HistoricoAcademicoDTO>> findByAlunoId(@PathVariable Long alunoId) {
        List<HistoricoAcademicoDTO> list = service.findByAlunoId(alunoId);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<HistoricoAcademicoDTO> insert(@Valid @RequestBody HistoricoAcademicoInsertDTO dto) {
        HistoricoAcademicoDTO newDto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoAcademicoDTO> update(@PathVariable Long id, @Valid @RequestBody HistoricoAcademicoInsertDTO dto) {
        HistoricoAcademicoDTO updatedDto = service.update(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}