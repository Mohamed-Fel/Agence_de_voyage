package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Paiement;
import com.example.demo.entities.Reservation;
import com.example.demo.enums.MethodePaiement;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
	 // Recherche un Paiement par l'ID de la réservation associée
    Optional<Paiement> findByReservationId(Long reservationId);
    
    // Si besoin, tu peux aussi ajouter :
    Optional<Paiement> findByReservation(Reservation reservation);
    List<Paiement> findByReservation_MethodePaiement(MethodePaiement methodePaiement);
    @Query("""
    	    SELECT MONTH(p.datePaiement), SUM(p.montantPaye)
    	    FROM Paiement p
    	    WHERE YEAR(p.datePaiement) = :annee
    	      AND p.paiementStatus = 'PAID'
    	    GROUP BY MONTH(p.datePaiement)
    	""")
    	List<Object[]> getRevenuParMois(@Param("annee") int annee);
}
