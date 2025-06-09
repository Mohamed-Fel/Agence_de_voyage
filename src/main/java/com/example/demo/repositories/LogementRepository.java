package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Logement;

public interface LogementRepository extends JpaRepository<Logement, Long> {
    Optional<Logement> findByName(String name);
    boolean existsByName(String name);
    List<Logement> findByRoom_Id(Long roomId);
    Optional<Logement> findByIdAndRoom_Id(Long logementId, Long roomId);
    List<Logement> findByNameAndPrix(String name, Double prix);
}