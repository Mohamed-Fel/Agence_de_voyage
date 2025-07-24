package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.CreateReservationRequest;
import com.example.demo.entities.Reservation;

public interface ReservationService {
	Reservation createReservation(CreateReservationRequest request);
	List<Reservation> getAllReservations(); 
	Reservation getReservationById(Long id);
	void deleteReservation(Long id);
	List<Reservation> getReservationsByNomHotel(String nomHotel);
    Reservation acceptReservation(Long reservationId);
    Reservation rejectReservation(Long reservationId);
}
