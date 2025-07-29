package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Paiement;
import com.example.demo.enums.MethodePaiement;
import com.example.demo.repositories.PaiementRepository;
import com.example.demo.services.PaiementService;
@Service
public class PaiementServiceImpl implements PaiementService {
	
	@Autowired
	private PaiementRepository paiementRepository;

    @Override
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'ID : " + id));
    }

    @Override
    public List<Paiement> getPaiementsByMethodePaiement(MethodePaiement methodePaiement) {
        return paiementRepository.findByReservation_MethodePaiement(methodePaiement);
    }
}
