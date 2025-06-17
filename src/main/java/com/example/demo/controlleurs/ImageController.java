package com.example.demo.controlleurs;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.ImageService;
import com.example.demo.entities.Image;
@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {
	
	
    @Autowired
    private ImageService imageService;;
    @GetMapping("/by-produit/{produitId}")
    public ResponseEntity<?> getImagesByProduitId(@PathVariable Long produitId) {
        try {
            List<Image> images = imageService.getImagesByProduitId(produitId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Images récupérées avec succès.");
            response.put("images", images);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la récupération des images : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<?> getImagesByRoomId(@PathVariable Long roomId) {
        try {
            List<Image> images = imageService.getImagesByRoomId(roomId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Images récupérées avec succès pour la chambre.");
            response.put("images", images);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la récupération des images pour la chambre : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImageById(@PathVariable Long imageId) {
        try {
            imageService.deleteImageById(imageId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "✅ Image supprimée avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la suppression de l'image : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}
