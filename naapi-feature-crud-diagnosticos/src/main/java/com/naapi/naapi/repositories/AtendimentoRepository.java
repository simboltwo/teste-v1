package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    List<Atendimento> findByResponsavelIdOrderByDataHoraDesc(Long responsavelId);
}