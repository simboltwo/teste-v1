/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/repositories/AlunoRepository.java
 * Descrição: Adicionados 3 novos métodos 'existsBy...' para verificação de integridade referencial.
 */
package com.naapi.naapi.repositories;

import com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO;
import com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO;
import com.naapi.naapi.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Importar Param
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long>, JpaSpecificationExecutor<Aluno> {

    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    @Query("SELECT new com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO(c.nome, COUNT(a.id)) "
        + "FROM Aluno a JOIN a.curso c "
        + "WHERE a.ativo = true "
        + "GROUP BY c.nome "
        + "ORDER BY COUNT(a.id) DESC")
    List<RelatorioAlunosPorCursoDTO> countAlunosPorCurso();

    @Query("SELECT new com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO(d.nome, COUNT(a.id)) "
        + "FROM Aluno a JOIN a.diagnosticos d "
        + "WHERE a.ativo = true "
        + "GROUP BY d.nome "
        + "ORDER BY COUNT(a.id) DESC")
    List<RelatorioAlunosPorDiagnosticoDTO> countAlunosPorDiagnostico();

    // --- INÍCIO DAS MUDANÇAS ---

    /**
     * Verifica se existe algum aluno (ativo ou inativo) associado a este Curso.
     */
    boolean existsByCursoId(Long cursoId);

    /**
     * Verifica se existe algum aluno (ativo ou inativo) associado a esta Turma.
     */
    boolean existsByTurmaId(Long turmaId);

    /**
     * Verifica (via Join) se existe algum aluno associado a este Diagnóstico.
     * Usamos uma Query customizada para checar a tabela de associação (ManyToMany).
     */
    @Query("SELECT COUNT(a) > 0 FROM Aluno a JOIN a.diagnosticos d WHERE d.id = :diagnosticoId")
    boolean existsByDiagnosticoId(@Param("diagnosticoId") Long diagnosticoId);
    
    // --- FIM DAS MUDANÇAS ---
}