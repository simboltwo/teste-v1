package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    boolean existsByNomeAndIdNot(String nome, Long id);
}
