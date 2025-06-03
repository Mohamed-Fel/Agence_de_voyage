package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Room;
import com.example.demo.services.RoomService;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rooms")
public class RoomController {
	@Autowired
    private RoomService roomService;

    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addRoom(
            @RequestParam("name") String name,
            @RequestParam("capacite") int capacite,
            @RequestParam("description") String description,
            @RequestParam("nbDeLit") int nbDeLit,
            @RequestParam("prixAdulte") double prixAdulte,
            @RequestParam("prixEnfant") double prixEnfant,
            @RequestParam("ageMinimal") int ageMinimal,
            @RequestParam("logementIds") List<Long> logementIds,
            @RequestParam("produitId") Long produitId,
            @RequestParam("images") List<MultipartFile> imageFiles
    ) {
        try {
            Room room = new Room();
            room.setName(name);
            room.setCapacite(capacite);
            room.setDescription(description);
            room.setNbDeLit(nbDeLit);
            room.setPrixAdulte(prixAdulte);
            room.setPrixEnfant(prixEnfant);
            room.setAgeMinimal(ageMinimal);

            Room savedRoom = roomService.addRoom(room, logementIds, produitId, imageFiles);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambre ajoutée avec succès.");
            response.put("room", savedRoom);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "❌ Erreur lors de l'ajout de la chambre : " + e.getMessage())
            );
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);

            return ResponseEntity.ok(
                Map.of(
                    "message", "✅ Chambre trouvée avec succès.",
                    "room", room
                )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "❌ " + e.getMessage())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                Map.of("error", "❌ " + e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                Map.of("error", "❌ Une erreur inattendue est survenue : " + e.getMessage())
            );
        }
    }
    @GetMapping("/by-produit")
    public ResponseEntity<?> getRoomsByProduitId(@RequestParam("produitId") Long produitId) {
        try {
            if (produitId == null || produitId <= 0) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "❌ L'ID du produit est invalide.")
                );
            }

            List<Room> rooms = roomService.getRoomsByProduitId(produitId);

            if (rooms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("error", "❌ Aucune chambre trouvée pour le produit ID : " + produitId)
                );
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambres trouvées avec succès.");
            response.put("rooms", rooms);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "❌ Erreur interne : " + e.getMessage())
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok(
                    Map.of("message", "✅ Chambre supprimée avec succès.")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "❌ Une erreur est survenue : " + e.getMessage())
            );
        }
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoom(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam int capacite,
        @RequestParam String description,
        @RequestParam int nbDeLit,
        @RequestParam double prixAdulte,
        @RequestParam double prixEnfant,
        @RequestParam int ageMinimal,
        @RequestParam Long produitId,
        @RequestParam(required = false) List<Long> logementIds,
        @RequestParam(required = false) List<MultipartFile> images
    ) {
        try {
            Room updatedRoom = roomService.updateRoom(
                id, name, capacite, description, nbDeLit, prixAdulte, prixEnfant,
                ageMinimal, produitId, logementIds, images
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambre mise à jour avec succès.");
            response.put("room", updatedRoom);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur serveur lors de la mise à jour : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            if (rooms.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ℹ️ Aucune chambre trouvée.");
                return ResponseEntity.ok(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Liste des chambres récupérée avec succès.");
            response.put("rooms", rooms);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la récupération des chambres : " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}


