package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Logement;

public interface LogementRepository extends JpaRepository<Logement, Long> {
    Optional<Logement> findByName(String name);
    boolean existsByName(String name);
}