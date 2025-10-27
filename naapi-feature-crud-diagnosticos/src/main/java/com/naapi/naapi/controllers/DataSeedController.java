package com.naapi.naapi.controllers;

import com.naapi.naapi.entities.Papel;
import com.naapi.naapi.repositories.PapelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
public class DataSeedController {

    private final PapelRepository papelRepository;

    @PostMapping("/seed-roles")
    public ResponseEntity<String> seedRoles() {
        if (papelRepository.count() > 0) {
            return ResponseEntity.ok("Papéis (Roles) já existem. Nenhuma ação foi tomada.");
        }

        Papel p1 = Papel.builder().authority("ROLE_COORDENADOR_NAAPI").build();
        Papel p2 = Papel.builder().authority("ROLE_MEMBRO_TECNICO").build();
        Papel p3 = Papel.builder().authority("ROLE_ESTAGIARIO_NAAPI").build();
        Papel p4 = Papel.builder().authority("ROLE_COORDENADOR_CURSO").build();
        Papel p5 = Papel.builder().authority("ROLE_PROFESSOR").build();

        papelRepository.saveAll(List.of(p1, p2, p3, p4, p5));

        return ResponseEntity.ok("SUCESSO: 5 papéis (roles) foram criados na base de dados.");
    }
}