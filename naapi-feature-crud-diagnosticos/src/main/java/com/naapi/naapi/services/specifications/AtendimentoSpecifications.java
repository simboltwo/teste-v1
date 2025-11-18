/*
 * Arquivo NOVO: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/services/specifications/AtendimentoSpecifications.java
 * Descrição: Permite a busca de atendimentos pelo nome do aluno.
 */
package com.naapi.naapi.services.specifications;

import com.naapi.naapi.entities.Aluno;
import com.naapi.naapi.entities.Atendimento;
import com.naapi.naapi.entities.Usuario;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AtendimentoSpecifications {

    /**
     * Filtra atendimentos pelo ID do responsável.
     */
    public static Specification<Atendimento> hasResponsavelId(Long responsavelId) {
        return (root, query, criteriaBuilder) -> {
            Join<Atendimento, Usuario> responsavelJoin = root.join("responsavel");
            return criteriaBuilder.equal(responsavelJoin.get("id"), responsavelId);
        };
    }

    /**
     * Filtra atendimentos contendo o nome do aluno (case-insensitive).
     */
    public static Specification<Atendimento> hasAlunoNome(String alunoNome) {
        return (root, query, criteriaBuilder) -> {
            Join<Atendimento, Aluno> alunoJoin = root.join("aluno");
            return criteriaBuilder.like(
                criteriaBuilder.lower(alunoJoin.get("nome")), 
                "%" + alunoNome.toLowerCase() + "%"
            );
        };
    }
}