package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{
	List<Image> findByProduitId(Long produitId);

}
