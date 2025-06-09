package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.example.demo.entities.Logement;
import com.example.demo.repositories.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.LogementService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/manageLogement")
public class LogementController {
	
    
	@Autowired
    private LogementService logementService;
	@Autowired
    private RoomRepository roomRepository;


    @GetMapping("/logements")
    public List<Logement> getAllLogements() {
        return logementService.getAllLogements();
    }

    @GetMapping("/logements/{id}")
    public ResponseEntity<?> getLogementById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Logement logement = logementService.getLogementById(id);
            response.put("message", "Logement avec l'ID " + id + " trouvé.");
            response.put("logement", logement);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/logements")
    public ResponseEntity<?> addLogement(@Valid @RequestBody Logement logement) {
        Map<String, Object> response = new HashMap<>();
        try {
            Logement saved = logementService.addLogement(logement);
            response.put("message", "Logement ajouté avec succès.");
            response.put("logement", saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MethodArgumentNotValidException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("error", "Erreur lors de l'ajout du logement : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/logements/{id}")
    public ResponseEntity<?> updateLogement(@PathVariable Long id, @RequestBody Logement logement) {
        Map<String, Object> response = new HashMap<>();
        try {
            Logement updated = logementService.updateLogement(id, logement);
            response.put("message", "Logement mis à jour avec succès.");
            response.put("logement", updated);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/logements/{id}")
    public ResponseEntity<?> deleteLogement(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            logementService.deleteLogement(id);
            response.put("message", "Logement supprimé avec succès.");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    // ✅ Get all logements by roomId
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getLogementsByRoomId(@PathVariable Long roomId) {
        try {
            List<Logement> logements = logementService.getLogementsByRoomId(roomId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Logements trouvés pour la chambre " + roomId);
            response.put("logements", logements);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ✅ Get specific logement by roomId and logementId
    @GetMapping("/room/{roomId}/logement/{logementId}")
    public ResponseEntity<?> getLogementByRoomIdAndLogementId(
        @PathVariable Long roomId,
        @PathVariable Long logementId
    ) {
    	Map<String, Object> response = new HashMap<>();
    	 // Vérifie si la room existe
        if (!roomRepository.existsById(roomId)) {
            response.put("error", "❌ La chambre avec l'ID " + roomId + " n'existe pas.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        try {
            Logement logement = logementService.getLogementByRoomIdAndLogementId(roomId, logementId);
            response.put("message", "✅ Logement trouvé pour la chambre " + roomId);
            response.put("logement", logement);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}