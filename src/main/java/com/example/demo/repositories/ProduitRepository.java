package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Produit;

public interface ProduitRepository  extends JpaRepository<Produit, Long>  {

    @Query("SELECT p FROM Produit p WHERE LOWER(p.categorie.name) = LOWER(:nomCategorie)")
    List<Produit> findByCategorieName(@Param("nomCategorie") String nomCategorie);
    
    @Query("SELECT p FROM Produit p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Produit> findByIdWithImages(@Param("id") Long id);

}

