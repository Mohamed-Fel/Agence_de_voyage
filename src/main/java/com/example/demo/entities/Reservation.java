package com.example.demo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.enums.MethodePaiement;
import com.example.demo.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude = "produit")
@EqualsAndHashCode(exclude = "produit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 private String emailClient;
	 private String phone;
	 private String firstName;
	 private String lastName;
	 private int age;
	 private String ville;
	 private String informationsSupplementaires;


	 private int nbNuitees;
	 private int nbAdultes;
	 private int nbEnfants;

	 private double totalPrixReservation;

	 private LocalDateTime checkIn;
	 private LocalDateTime checkOut;
	 @Enumerated(EnumType.STRING)
	 @Column(length = 20)
	 private ReservationStatus status; // ex: PENDING, CONFIRMED, CANCELLED

	 private LocalDateTime dateDeReservation;
	 @Enumerated(EnumType.STRING)
	 private MethodePaiement methodePaiement; // "EN_LIGNE" ou "A_L_AGENCE"
	 
	 @ManyToMany
	 @JoinTable(
	     name = "reservation_rooms",
	     joinColumns = @JoinColumn(name = "reservation_id"),
	     inverseJoinColumns = @JoinColumn(name = "room_id")
	 )
	 @JsonManagedReference
	 private List<Room> rooms;

	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "paiement_id", referencedColumnName = "id")
	 @JsonManagedReference
	 private Paiement paiement;
	 private String ficheReservationUrl;
	 
}
