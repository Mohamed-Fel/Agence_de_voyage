package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Paiement;
import com.example.demo.enums.MethodePaiement;

public interface PaiementService {
    List<Paiement> getAllPaiements();
    Paiement getPaiementById(Long id);
    List<Paiement> getPaiementsByMethodePaiement(MethodePaiement methodePaiement);
}
