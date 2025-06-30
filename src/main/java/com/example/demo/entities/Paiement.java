package com.example.demo.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@ToString(exclude = "reservation")
@EqualsAndHashCode(exclude = "reservation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePaiement;

    private String paiementStatus; // ex: SUCCESS, FAILED, PENDING

    private String paymentType;  // ex: Carte bancaire, Flouci, Paypal

    private double montantPaye;
    
    @OneToOne(mappedBy = "paiement")
    @JsonBackReference
    private Reservation reservation;

}
