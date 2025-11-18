package com.naapi.naapi.services;

import com.naapi.naapi.dtos.PapelDTO;
import com.naapi.naapi.entities.Papel;
import com.naapi.naapi.repositories.PapelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PapelService {

    private final PapelRepository repository;

    /**
     * Busca todos os papéis cadastrados no sistema.
     */
    @Transactional(readOnly = true)
    public List<PapelDTO> findAll() {
        List<Papel> list = repository.findAll();
        // Converte a lista de Entidades Papel para uma lista de PapelDTO
        return list.stream().map(PapelDTO::new).collect(Collectors.toList());
    }
}