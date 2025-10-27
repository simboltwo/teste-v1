package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Laudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaudoRepository extends JpaRepository<Laudo, Long> {

    List<Laudo> findByAlunoId(Long alunoId);
}