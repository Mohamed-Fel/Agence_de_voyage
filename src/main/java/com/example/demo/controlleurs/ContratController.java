package com.example.demo.controlleurs;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Contrat;
import com.example.demo.services.ContratService;

@RestController
@RequestMapping("/contrats")
@CrossOrigin(origins = "http://localhost:4200")
public class ContratController {

    @Autowired
    private ContratService contratService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addContrat(
            @RequestParam("nomProduit") String nomProduit,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestPart("file") MultipartFile file) {
        try {
            Contrat contrat = contratService.addContrat(nomProduit, startDate, endDate, file);
            return ResponseEntity.ok(Map.of("message", "Contrat ajouté avec succès", "contrat", contrat));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/non-assignes")
    public ResponseEntity<?> getContratsNonAssignes() {
        try {
            List<Contrat> contrats = contratService.getContratsNonAssignes();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Contrats non assignés récupérés avec succès.");
            response.put("contrats", contrats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la récupération des contrats : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateContrat(
            @PathVariable Long id,
            @RequestParam("nomProduit") String nomProduit,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Contrat contrat = contratService.updateContrat(id, nomProduit, startDate, endDate, file);
            return ResponseEntity.ok(Map.of("message", "Contrat mis à jour", "contrat", contrat));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteContrat(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            contratService.deleteContrat(id);
            response.put("success", true);
            response.put("message", "Le contrat a été supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("success", false);
            response.put("message", "Contrat introuvable avec l'ID : " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Une erreur est survenue lors de la suppression du contrat.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getContratById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Contrat contrat = contratService.getContratById(id);
            
            response.put("message", "Contrat récupéré avec succès.");
            response.put("contrat", contrat);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            
            response.put("message", "Contrat introuvable avec l'ID : " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            
            response.put("message", "Une erreur est survenue lors de la récupération du contrat.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public List<Contrat> getAllContrats() {
        return contratService.getAllContrats();
    }

    @GetMapping("/hotel/{nomHotel}")
    public ResponseEntity<?> getContratByNomHotel(@PathVariable String nomHotel) {
        try {
            Contrat contrat = contratService.getContratByNomProduit(nomHotel);
            return ResponseEntity.ok(contrat);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
