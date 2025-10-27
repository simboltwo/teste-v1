package com.naapi.naapi.services.specifications;

import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Curso;
import com.naapi.naapi.entities.Diagnostico;
import com.naapi.naapi.entities.Turma;
import jakarta.persistence.criteria.Join;
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
}