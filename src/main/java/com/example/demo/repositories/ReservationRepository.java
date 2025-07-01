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
	
	@Query("SELECT COUNT(r) FROM Reservation r WHERE DATE(r.dateDeReservation) = CURRENT_DATE")
	Long countTotalReservationsToday();
	
	@Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status")
	List<Object[]> countReservationsByStatus();
	
	@Query("SELECT r.methodePaiement, COUNT(r) FROM Reservation r GROUP BY r.methodePaiement")
	List<Object[]> countReservationsByPaymentMethod();
	
    @Query("""
            select p.name, count(r.id)
            from Reservation r
            join r.rooms room
            join room.produit p
            where r.dateDeReservation >= :startDate
              and r.dateDeReservation < :endDate
            group by p.name
            order by count(r.id) desc
            """)
        List<Object[]> findTop3ProduitsReservedInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

        @Query("""
            select count(r.id) > 0
            from Reservation r
            where r.dateDeReservation >= :startDate
              and r.dateDeReservation < :endDate
            """)
        boolean existsReservationsInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
        
        @Query("""
        	    SELECT p.ville, COUNT(r.id)
        	    FROM Reservation r
        	    JOIN r.rooms room
        	    JOIN room.produit p
        	    GROUP BY p.ville
        	    ORDER BY COUNT(r.id) DESC
        	""")
        	List<Object[]> findTop3Destinations();

}
