package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Localisation;
import com.example.demo.entities.Produit;
import com.example.demo.services.ProduitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/produits")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProduitController {
	
	@Autowired
    private ProduitService produitService;

    @PostMapping(value = "/produit",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduit(
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam String phoneNumber,
        @RequestParam String email,
        @RequestParam int nbEtoiles,
        @RequestParam String pays,
        @RequestParam String ville,
        @RequestParam String adresse,
        @RequestParam int initialPrix,
        @RequestParam Long categorieId,
        @RequestParam Long contratId,
        @RequestParam List<Long> serviceIds,
        @RequestParam("images") List<MultipartFile> images,
        @RequestParam Long creatorId,
        @RequestParam double latitude,
        @RequestParam double longitude
    ) {
        try {
        	Localisation localisation = new Localisation();
            localisation.setLatitude(latitude);
            localisation.setLongitude(longitude);
            
            Produit produit = produitService.createProduit(
                name, description, phoneNumber, email, nbEtoiles, pays, ville, adresse,
                initialPrix, categorieId, contratId, serviceIds, images, creatorId ,localisation
            );
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Produit créé avec succès.");
            response.put("produit", produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
        	 Map<String, String> error = new HashMap<>();
             error.put("error", "❌ Erreur lors de la création : " + e.getMessage());
             return ResponseEntity.badRequest().body(error);
        }
    }
    @PutMapping(value = "/produit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduit(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam String phoneNumber,
        @RequestParam String email,
        @RequestParam int nbEtoiles,
        @RequestParam String pays,
        @RequestParam String ville,
        @RequestParam String adresse,
        @RequestParam int initialPrix,
        @RequestParam(required = false) Long categorieId,
        @RequestParam(required = false) Long contratId,
        @RequestParam(required = false) List<Long> serviceIds,
        @RequestParam(required = false, name = "images") List<MultipartFile> images,
        @RequestParam(required = false) Long creatorId,
        @RequestParam double latitude,
        @RequestParam double longitude
    ) {
        try {
        	
        	Localisation localisation = new Localisation();
            localisation.setLatitude(latitude);
            localisation.setLongitude(longitude);
            Produit updated = produitService.updateProduit(
                id, name, description, phoneNumber, email, nbEtoiles,
                pays, ville, adresse, initialPrix,
                categorieId, contratId, serviceIds, images, creatorId ,localisation
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Produit mis à jour avec succès.");
            response.put("produit", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Pour les erreurs comme numéro invalide
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Données invalides : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la mise à jour : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduitById(@PathVariable Long id) {
        Optional<Produit> optionalProduit = produitService.findById(id); // à implémenter dans ProduitService

        if (optionalProduit.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Produit avec l'ID " + id + " trouvé.");
            response.put("produit", optionalProduit.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "❌ Produit avec l'ID " + id + " non trouvé.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    @DeleteMapping("/produit/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduit(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            produitService.deleteProduit(id);
            response.put("success", true);
            response.put("message", "✅ Produit supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("success", false);
            response.put("message", "❌ Produit introuvable avec l'ID : " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Une erreur est survenue lors de la suppression du produit.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/by-categorie")
    public ResponseEntity<?> getProduitsByCategorieName(@RequestParam String nomCategorie) {
        List<Produit> produits = produitService.getProduitsByCategorieName(nomCategorie);
        
        if (produits.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "❌ Aucun produit trouvé pour la catégorie : " + nomCategorie);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Produits trouvés pour la catégorie : " + nomCategorie);
        response.put("produits", produits);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllProduits() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Produit> produits = produitService.getAllProduits();
            response.put("success", true);
            response.put("produits", produits);
            response.put("message", "✅ Liste des produits récupérée avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Erreur lors de la récupération des produits.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}