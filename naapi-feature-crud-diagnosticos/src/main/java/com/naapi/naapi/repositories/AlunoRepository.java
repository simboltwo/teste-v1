package com.naapi.naapi.repositories;

import com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO;
import com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO;
import com.naapi.naapi.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    @Query("SELECT new com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO(c.nome, COUNT(a.id)) "
        + "FROM Aluno a JOIN a.curso c "
        + "GROUP BY c.nome "
        + "ORDER BY totalAlunos DESC")
    List<RelatorioAlunosPorCursoDTO> countAlunosPorCurso();

    @Query("SELECT new com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO(d.nome, COUNT(a.id)) "
        + "FROM Aluno a JOIN a.diagnosticos d "
        + "GROUP BY d.nome "
        + "ORDER BY totalAlunos DESC")
    List<RelatorioAlunosPorDiagnosticoDTO> countAlunosPorDiagnostico();
}