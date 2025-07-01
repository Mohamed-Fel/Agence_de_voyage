package com.example.demo.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.enums.MethodePaiement;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {

    // 📌 Infos personnelles
    private String emailClient;
    private String phone;
    private String firstName;
    private String lastName;
    private int age;
    private String ville;
    private String informationsSupplementaires;

    // 📌 Détails réservation
    private int nbNuitees;
    private int nbAdultes;
    private int nbEnfants;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    
    private LocalDateTime dateDeReservation;
    private List<Long> roomIds;
    private int montantapayer;

    // 📌 Paiement
    @Enumerated(EnumType.STRING)
    private MethodePaiement methodePaiement; // "EN_LIGNE" ou "A_L_AGENCE"

}
