package com.example.demo.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Reservation;
import com.example.demo.enums.MethodePaiement;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.repositories.ReservationRepository;

import jakarta.transaction.Transactional;

@Component
public class ReservationScheduler {
	
	@Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(cron = "0 0 * * * *") // toutes les heures
    public void updateReservationStatuses() {
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {
            ReservationStatus newStatus = determineStatus(r.getCheckIn(), r.getCheckOut());
            if (!r.getStatus().equals(newStatus)) {
                r.setStatus(newStatus);
                reservationRepository.save(r);
            }
        }
    }

    private ReservationStatus determineStatus(LocalDateTime checkIn, LocalDateTime checkOut) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(checkIn)) {
            return ReservationStatus.A_VENIR;
        } else if (now.isAfter(checkIn) && now.isBefore(checkOut)) {
            return ReservationStatus.EN_COURS;
        } else {
            return ReservationStatus.TERMINEE;
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // tous les jours à minuit
    @Transactional
    public void deleteUnpaidAgencyReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation reservation : reservations) {
            if (reservation.getMethodePaiement() == MethodePaiement.A_L_AGENCE) {
                if (reservation.getDateDeReservation().plusDays(2).isBefore(now)) {
                    reservationRepository.delete(reservation);
                    System.out.println("❌ Réservation supprimée automatiquement : ID = " + reservation.getId());
                }
            }
        }
    }
}