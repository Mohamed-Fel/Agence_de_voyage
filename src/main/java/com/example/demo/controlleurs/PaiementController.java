package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Paiement;
import com.example.demo.enums.MethodePaiement;
import com.example.demo.services.PaiementService;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {
	
	@Autowired
    private PaiementService paiementService;

    @GetMapping
    public ResponseEntity<?> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        Map<String, Object> response = new HashMap<>();

        if (paiements.isEmpty()) {
            response.put("message", "Aucun paiement trouvé.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        response.put("message", "Liste des paiements récupérée avec succès.");
        response.put("paiements", paiements);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaiementById(@PathVariable Long id) {
        Optional<Paiement> optionalPaiement = Optional.ofNullable(paiementService.getPaiementById(id));
        Map<String, Object> response = new HashMap<>();

        if (optionalPaiement.isPresent()) {
            response.put("message", "Paiement avec l'ID " + id + " trouvé.");
            response.put("paiement", optionalPaiement.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Paiement avec l'ID " + id + " introuvable.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/methode/{methodePaiement}")
    public ResponseEntity<?> getPaiementsByMethodePaiement(@PathVariable MethodePaiement methodePaiement) {
        List<Paiement> paiements = paiementService.getPaiementsByMethodePaiement(methodePaiement);
        Map<String, Object> response = new HashMap<>();

        if (paiements.isEmpty()) {
            response.put("message", "Aucun paiement trouvé pour la méthode : " + methodePaiement);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("message", "Paiements trouvés pour la méthode : " + methodePaiement);
        response.put("paiements", paiements);
        return ResponseEntity.ok(response);
    }
}