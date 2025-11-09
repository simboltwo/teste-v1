package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long>{

    boolean existsByNomeAndIdNot(String nome, Long id);

    boolean existsByCidAndIdNot(String cid, Long id);

    boolean existsBySiglaAndIdNot(String sigla, Long id);
}