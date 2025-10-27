package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, Long> {

    boolean existsByNomeAndIdNot(String nome, Long id);
}
