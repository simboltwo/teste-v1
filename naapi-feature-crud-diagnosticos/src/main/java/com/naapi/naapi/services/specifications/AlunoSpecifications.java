package com.naapi.naapi.services.specifications;

import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Curso;
import com.naapi.naapi.entities.Diagnostico;
import com.naapi.naapi.entities.Turma;

import com.naapi.naapi.entities.Atendimento; 
import jakarta.persistence.criteria.Join;
import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class AlunoSpecifications {

    public static Specification<Aluno> hasNome(String nome) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Aluno> hasMatricula(String matricula) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("matricula"), matricula);
    }

    public static Specification<Aluno> hasCursoId(Long cursoId) {
        return (root, query, criteriaBuilder) -> {
            Join<Aluno, Curso> cursoJoin = root.join("curso");
            return criteriaBuilder.equal(cursoJoin.get("id"), cursoId);
        };
    }

    public static Specification<Aluno> hasTurmaId(Long turmaId) {
        return (root, query, criteriaBuilder) -> {
            Join<Aluno, Turma> turmaJoin = root.join("turma");
            return criteriaBuilder.equal(turmaJoin.get("id"), turmaId);
        };
    }

    public static Specification<Aluno> hasDiagnosticoId(Long diagnosticoId) {
        return (root, query, criteriaBuilder) -> {
            Join<Aluno, Diagnostico> diagnosticoJoin = root.join("diagnosticos");
            return criteriaBuilder.equal(diagnosticoJoin.get("id"), diagnosticoId);
        };
    }

    public static Specification<Aluno> hasCursoIds(List<Long> cursoIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Aluno, Curso> cursoJoin = root.join("curso");
            return cursoJoin.get("id").in(cursoIds); 
        };
    }

    public static Specification<Aluno> hasDiagnosticoIds(List<Long> diagnosticoIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Aluno, Diagnostico> diagnosticoJoin = root.join("diagnosticos");
            // Usa "in" em vez de "equal"
            return diagnosticoJoin.get("id").in(diagnosticoIds);
        };
    }

    public static Specification<Aluno> hasAtendimentoAgendadoParaData(LocalDate data, String status) {
        return (root, query, criteriaBuilder) -> {
            // se um aluno tiver múltiplos atendimentos no dia, ele apareça apenas 1 vez
            query.distinct(true); 
            
            Join<Aluno, Atendimento> atendimentoJoin = root.join("atendimentos");
            
            LocalDateTime startOfDay = data.atStartOfDay();
            LocalDateTime endOfDay = data.atTime(LocalTime.MAX);
            
            return criteriaBuilder.and(
                criteriaBuilder.equal(atendimentoJoin.get("status"), status.toUpperCase()),
                criteriaBuilder.between(atendimentoJoin.get("dataHora"), startOfDay, endOfDay)
            );
        };
    }
}