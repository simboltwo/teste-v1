/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/repositories/AtendimentoRepository.java
 * Descrição: Adicionado 'findByAlunoIdAndStatusOrderByDataHoraDesc' e 'findByResponsavelIdOrderByDataHoraDesc'.
 */
package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    List<Atendimento> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    // --- INÍCIO DA MUDANÇA ---
    /**
     * Busca atendimentos de um aluno filtrando por status (ex: "AGENDADO").
     */
    List<Atendimento> findByAlunoIdAndStatusOrderByDataHoraDesc(Long alunoId, String status);

    /**
     * Busca atendimentos de um profissional (responsável).
     */
    List<Atendimento> findByResponsavelIdOrderByDataHoraDesc(Long responsavelId);
    // --- FIM DA MUDANÇA ---

    /**
     * Conta o número total de atendimentos com um status específico.
     * @param status O status (ex: "REALIZADO", "AGENDADO")
     * @return A contagem total.
     */
    long countByStatus(String status);
}