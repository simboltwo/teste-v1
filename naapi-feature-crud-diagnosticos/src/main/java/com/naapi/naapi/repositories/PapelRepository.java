package com.naapi.naapi.repositories;

import com.naapi.naapi.entities.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {
    
    Papel findByAuthority(String authority);
}