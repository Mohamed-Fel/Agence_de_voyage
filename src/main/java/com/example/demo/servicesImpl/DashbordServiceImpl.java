package com.example.demo.servicesImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.enums.MethodePaiement;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.repositories.PaiementRepository;
import com.example.demo.repositories.ProduitRepository;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.services.DashbordService;
@Service
public class DashbordServiceImpl implements DashbordService {
	@Autowired
    private ReservationRepository reservationRepository;
	@Autowired
	private ProduitRepository produitRepository;
	@Autowired
	private PaiementRepository paiementRepository;
    @Override
    public Long getTotalBookingsToday() {
        return reservationRepository.countTotalReservationsToday();
    }
    
    /*@Override
    public Map<String, Long> getReservationCountByStatus() {
        List<Object[]> results = reservationRepository.countReservationsByStatus();
        Map<String, Long> stats = new HashMap<>();

        for (Object[] row : results) {
            ReservationStatus status = (ReservationStatus) row[0];
            Long count = (Long) row[1];
            stats.put(status.name(), count);
        }

        return stats;
    }*/
    @Override
    public Map<String, Long> getReservationCountByStatus() {
        // Initialiser la map avec tous les statuts à 0
        Map<String, Long> stats = new HashMap<>();
        for (ReservationStatus status : ReservationStatus.values()) {
            stats.put(status.name(), 0L);
        }

        // Récupérer les résultats existants en base
        List<Object[]> results = reservationRepository.countReservationsByStatus();

        // Mettre à jour les valeurs présentes
        for (Object[] row : results) {
            ReservationStatus status = (ReservationStatus) row[0];
            Long count = (Long) row[1];
            stats.put(status.name(), count);
        }

        return stats;
    }
    /*@Override
    public Map<String, Long> getReservationCountByPaymentMethod() {
        List<Object[]> results = reservationRepository.countReservationsByPaymentMethod();
        Map<String, Long> stats = new HashMap<>();

        for (Object[] row : results) {
            MethodePaiement method = (MethodePaiement) row[0];
            Long count = (Long) row[1];
            stats.put(method.name(), count);
        }

        return stats;
    }*/
    @Override
    public Map<String, Long> getReservationCountByPaymentMethod() {
        // Initialiser la map avec toutes les méthodes à 0
        Map<String, Long> stats = new HashMap<>();
        for (MethodePaiement method : MethodePaiement.values()) {
            stats.put(method.name(), 0L);
        }

        // Récupérer les résultats existants en base
        List<Object[]> results = reservationRepository.countReservationsByPaymentMethod();

        // Mettre à jour les valeurs présentes
        for (Object[] row : results) {
            MethodePaiement method = (MethodePaiement) row[0];
            Long count = (Long) row[1];
            stats.put(method.name(), count);
        }

        return stats;
    }
    
    @Override
    public List<Object[]> getTop3ProduitsReservedInMonth(int year, int month) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1);

        List<Object[]> results = reservationRepository.findTop3ProduitsReservedInPeriod(startDate, endDate);
        // Limiter à 3 en Java
        return results.size() > 3 ? results.subList(0, 3) : results;
    }

    @Override
    public boolean checkIfReservationsExistInMonth(int year, int month) {
        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1);

        return reservationRepository.existsReservationsInPeriod(startDate, endDate);
    }
    
    @Override
    public List<Map<String, Object>> getTop3Destinations() {
        List<Object[]> results = reservationRepository.findTop3Destinations();

        // Limiter à 3 et formater en Map pour un retour JSON propre
        return results.stream()
                .limit(3)
                .map(row -> Map.of(
                        "ville", row[0],
                        "nombreReservations", row[1]
                ))
                .collect(Collectors.toList());
    }
    @Override
    public Map<String, Object> countProduitsByCategorieName(String name) {
        Long total = produitRepository.countByCategorieName(name);
        Map<String, Object> result = new HashMap<>();
        result.put("categorie", name);
        result.put("total", total);
        return result;
    }
    @Override
    public Map<Integer, Double> getRevenuMensuel(int annee) {
        List<Object[]> resultats = paiementRepository.getRevenuParMois(annee);
        Map<Integer, Double> revenus = new HashMap<>();

        for (Object[] obj : resultats) {
            Integer mois = (Integer) obj[0];
            Double montant = (Double) obj[1];
            revenus.put(mois, montant);
        }

        return revenus;
    }

}
