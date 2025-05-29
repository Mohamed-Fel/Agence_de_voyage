package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Contrat;

public interface ContratRepository extends JpaRepository<Contrat, Long>{
	Optional<Contrat> findByNomHotel(String nomHotel);
    List<Contrat> findAll();

}
