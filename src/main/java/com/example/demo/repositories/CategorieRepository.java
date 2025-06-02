package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    Optional<Categorie> findByName(String name);
}