package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.PEI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeiRepository extends JpaRepository<PEI, Long> {

    List<PEI> findByAlunoId(Long alunoId);
}