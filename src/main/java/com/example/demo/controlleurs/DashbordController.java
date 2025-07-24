package com.example.demo.controlleurs;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.DashbordService;

@RestController
@RequestMapping("/api/dashboard")
public class DashbordController {
	@Autowired
    private DashbordService dashbordService;

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

}
