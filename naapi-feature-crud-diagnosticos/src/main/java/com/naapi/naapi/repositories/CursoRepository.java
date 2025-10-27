package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long>{

    boolean existsByNomeAndIdNot(String nome, Long id);
}