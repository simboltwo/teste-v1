/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/repositories/AtendimentoRepository.java
 * Descrição: Repositório agora estende JpaSpecificationExecutor.
 */
package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // --- MUDANÇA ---
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>, JpaSpecificationExecutor<Atendimento> { // --- MUDANÇA ---

    List<Atendimento> findByAlunoIdOrderByDataHoraDesc(Long alunoId);

    List<Atendimento> findByAlunoIdAndStatusOrderByDataHoraDesc(Long alunoId, String status);

    List<Atendimento> findByResponsavelIdOrderByDataHoraDesc(Long responsavelId);

    long countByStatus(String status);
}