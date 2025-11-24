package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.HistoricoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoAcademicoRepository extends JpaRepository<HistoricoAcademico, Long> {

    List<HistoricoAcademico> findByAlunoIdOrderByDataInicioAsc(Long alunoId);
}