package com.example.demo.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.Room;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	@Query("SELECT r.rooms FROM Reservation r WHERE " +
		       "(r.checkIn < :checkOut AND r.checkOut > :checkIn)")
		List<Room> findReservedRoomsBetween(@Param("checkIn") LocalDateTime checkIn,
		                                     @Param("checkOut") LocalDateTime checkOut);
	@Query("SELECT r FROM Reservation r JOIN r.rooms room WHERE room.produit.name = :nomHotel")
	List<Reservation> findByNomHotel(@Param("nomHotel") String nomHotel);

}
