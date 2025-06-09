package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Contrat;

public interface ContratRepository extends JpaRepository<Contrat, Long>{
	Optional<Contrat> findByNomProduit(String nomProduit);
    List<Contrat> findAll();
   
    @Query("SELECT c FROM Contrat c WHERE c.id NOT IN (SELECT p.contrat.id FROM Produit p WHERE p.contrat IS NOT NULL)")
    List<Contrat> findContratsNotAssignedToProduit();

}
