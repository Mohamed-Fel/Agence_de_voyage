package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Produit;

public interface ProduitService {
	Produit createProduit(
	        String name, String description, String phoneNumber, String email, int nbEtoiles,
	        String pays, String ville, String adresse, int initialPrix, Long categorieId,
	        Long contratId, List<Long> serviceIds, List<MultipartFile> images, Long creatorId
	    ) throws Exception;
	Produit updateProduit(
	        Long id, String name, String description, String phoneNumber, String email, int nbEtoiles,
	        String pays, String ville, String adresse, int initialPrix, Long categorieId,
	        Long contratId, List<Long> serviceIds, List<MultipartFile> images, Long creatorId
	    ) throws Exception;
	Optional<Produit> findById(Long id);
	List<Produit> getProduitsByCategorieName(String nomCategorie);
	void deleteProduit(Long id);
	List<Produit> getAllProduits();
}


