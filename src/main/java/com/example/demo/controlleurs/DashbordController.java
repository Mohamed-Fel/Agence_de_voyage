package com.example.demo.controlleurs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.DashbordService;
import com.example.demo.services.PaiementService;

@RestController
@RequestMapping("/api/dashboard")
public class DashbordController {
	@Autowired
    private DashbordService dashbordService;
	
	@Autowired
    private PaiementService paiementService;
	

    @GetMapping("/total-bookings-today")
    public ResponseEntity<Long> getTotalBookingsToday() {
        Long total = dashbordService.getTotalBookingsToday();
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/reservations-by-status")
    public ResponseEntity<Map<String, Long>> getReservationsByStatus() {
        return ResponseEntity.ok(dashbordService.getReservationCountByStatus());
    }
    
    @GetMapping("/reservations-by-payment")
    public ResponseEntity<Map<String, Long>> getReservationsByPaymentMethod() {
        return ResponseEntity.ok(dashbordService.getReservationCountByPaymentMethod());
    }
    @GetMapping("/top3-produits")
    public List<Map<String, Object>> top3Produits(
            @RequestParam int year,
            @RequestParam int month) {

        List<Object[]> results = dashbordService.getTop3ProduitsReservedInMonth(year, month);

        // Transformer les Object[] en Map pour un JSON clair
        return results.stream()
                .map(row -> Map.of(
                        "produit", row[0],
                        "nombreReservations", row[1]
                ))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/top3-destinations")
    public List<Map<String, Object>> getTop3Destinations() {
        return dashbordService.getTop3Destinations();
    }
    @GetMapping("/categorie")
    public Map<String, Object> getNombreProduitsParCategorie(@RequestParam String name) {
        return dashbordService.countProduitsByCategorieName(name);
    }
    @GetMapping("/revenu-annuel/{annee}")
    public ResponseEntity<Map<String, Object>> getRevenuMensuel(@PathVariable int annee) {
        Map<Integer, Double> revenus = dashbordService.getRevenuMensuel(annee);

        String[] moisNoms = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };

        List<String> affichage = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            double montant = revenus.getOrDefault(i, 0.0);
            affichage.add(moisNoms[i - 1] + " : " + montant);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("revenu", affichage);
        response.put("message", "Revenu mensuel pour l'année " + annee + " récupéré avec succès.");

        return ResponseEntity.ok(response);
    }

}
