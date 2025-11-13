package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    List<Atendimento> findByResponsavelIdOrderByDataHoraDesc(Long responsavelId);

    // --- INÍCIO DA MUDANÇA ---
    /**
     * Conta o número total de atendimentos com um status específico.
     * @param status O status (ex: "REALIZADO", "AGENDADO")
     * @return A contagem total.
     */
    long countByStatus(String status);
    // --- FIM DA MUDANÇA ---
}