package com.example.demo.servicesImpl;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.CreateReservationRequest;
import com.example.demo.entities.Reservation;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.repositories.RoomRepository;
import com.example.demo.entities.Room;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.services.ReservationService;
@Service
public class ReservationServiceImpl implements ReservationService {
	
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;
    
    private ReservationStatus determineReservationStatus(LocalDateTime checkIn, LocalDateTime checkOut) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(checkIn)) {
            return ReservationStatus.A_VENIR;
        } else if (now.isAfter(checkIn) && now.isBefore(checkOut)) {
            return ReservationStatus.EN_COURS;
        } else {
            return ReservationStatus.TERMINEE;
        }
    }

    @Override
    public Reservation createReservation(CreateReservationRequest request) {
        // 🧠 Récupérer les chambres à partir des IDs
        List<Room> rooms = roomRepository.findAllById(request.getRoomIds());

        // 🧱 Créer l'objet Reservation
        Reservation reservation = new Reservation();
        reservation.setEmailClient(request.getEmailClient());
        reservation.setPhone(request.getPhone());
        reservation.setFirstName(request.getFirstName());
        reservation.setLastName(request.getLastName());
        reservation.setAge(request.getAge());
        reservation.setVille(request.getVille());
        reservation.setInformationsSupplementaires(request.getInformationsSupplementaires());
        reservation.setNbNuitees(request.getNbNuitees());
        reservation.setNbAdultes(request.getNbAdultes());
        reservation.setNbEnfants(request.getNbEnfants());
        reservation.setCheckIn(request.getCheckIn());
        reservation.setCheckOut(request.getCheckOut());
        reservation.setDateDeReservation(request.getDateDeReservation()); // ou request.getDateDeReservation()
        reservation.setTotalPrixReservation(request.getMontantapayer());
        reservation.setStatus(determineReservationStatus(request.getCheckIn(), request.getCheckOut()));
        reservation.setRooms(rooms);

        // 💾 Sauvegarder la réservation ET retourner l'entité sauvegardée
        return reservationRepository.save(reservation);
    }
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Aucune réservation trouvée avec l'ID : " + id));
    }
    @Override
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Aucune réservation trouvée avec l'ID : " + id));

        reservationRepository.delete(reservation);
    }
    @Override
    public List<Reservation> getReservationsByNomHotel(String nomHotel) {
        return reservationRepository.findByNomHotel(nomHotel);
    }
}
