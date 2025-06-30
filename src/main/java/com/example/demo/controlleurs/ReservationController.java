package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.CreateReservationRequest;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;
import com.example.demo.services.ReservationService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/Reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    
	@PostMapping("/createRev")
	public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest request) {
	    try {
	        Reservation reservation = reservationService.createReservation(request);

	        // Extraire uniquement les IDs des rooms
	        /*List<Long> roomIds = reservation.getRooms()
	                                        .stream()
	                                        .map(Room::getId)
	                                        .toList();*/
	        String hotelName = "Inconnu";
	        if (!reservation.getRooms().isEmpty()) {
	            Room firstRoom = reservation.getRooms().get(0);
	            if (firstRoom.getProduit() != null) {
	                hotelName = firstRoom.getProduit().getName();
	            }
	        }

	        // Créer une map de réponse personnalisée
	        Map<String, Object> reservationMap = new HashMap<>();
	        reservationMap.put("id", reservation.getId());
	        reservationMap.put("emailClient", reservation.getEmailClient());
	        reservationMap.put("phone", reservation.getPhone());
	        reservationMap.put("firstName", reservation.getFirstName());
	        reservationMap.put("lastName", reservation.getLastName());
	        reservationMap.put("age", reservation.getAge());
	        reservationMap.put("ville", reservation.getVille());
	        reservationMap.put("informationsSupplementaires", reservation.getInformationsSupplementaires());
	        reservationMap.put("nbNuitees", reservation.getNbNuitees());
	        reservationMap.put("nbAdultes", reservation.getNbAdultes());
	        reservationMap.put("nbEnfants", reservation.getNbEnfants());
	        reservationMap.put("checkIn", reservation.getCheckIn());
	        reservationMap.put("checkOut", reservation.getCheckOut());
	        reservationMap.put("dateDeReservation", reservation.getDateDeReservation());
	        reservationMap.put("totalPrixReservation", reservation.getTotalPrixReservation());
	        reservationMap.put("status", reservation.getStatus());
	        reservationMap.put("roomIds", reservation.getRooms().stream().map(Room::getId).toList());
	        reservationMap.put("hotelName", hotelName);
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "✅ Réservation créée avec succès.");
	        response.put("reservation", reservationMap);
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(
	                Map.of("error", "❌ Données invalides : " + e.getMessage())
	        );
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	                Map.of("error", "❌ Erreur interne lors de la création de la réservation : " + e.getMessage())
	        );
	    }
	}
    // ✅ GET ALL RESERVATIONS
    @GetMapping("/all")
    public ResponseEntity<?> getAllReservations() {
        try {
            List<Reservation> reservations = reservationService.getAllReservations();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "❌ Erreur lors de la récupération des réservations"));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "❌ Erreur interne : " + e.getMessage())
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok(Map.of("message", "✅ Réservation supprimée avec succès."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "❌ Erreur interne lors de la suppression : " + e.getMessage())
            );
        }
    }
    @GetMapping("/byNamehotel")
    public ResponseEntity<?> getReservationsByHotel(@RequestParam String nomHotel) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByNomHotel(nomHotel);
            if (reservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("message", "❌ Aucune réservation trouvée pour l'hôtel : " + nomHotel)
                );
            }
            return ResponseEntity.ok(Map.of(
                    "message", "✅ Réservations récupérées avec succès.",
                    "reservations", reservations
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "❌ Erreur lors de la récupération des réservations : " + e.getMessage())
            );
        }
    }

}
