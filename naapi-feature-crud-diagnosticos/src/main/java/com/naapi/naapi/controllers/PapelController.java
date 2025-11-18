package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.PapelDTO;
import com.naapi.naapi.services.PapelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/papeis")
@RequiredArgsConstructor
public class PapelController {

    private final PapelService service;

    /**
     * Endpoint para o frontend buscar a lista de todos os papéis
     * disponíveis para associar a um usuário.
     */
    @GetMapping
    public ResponseEntity<List<PapelDTO>> findAll() {
        List<PapelDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }
}