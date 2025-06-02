package com.example.demo.servicesImpl;

import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.stereotype.Service;

import com.example.demo.entities.Logement;
import com.example.demo.repositories.LogementRepository;
import com.example.demo.services.LogementService;

@Service
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;

    public LogementServiceImpl(LogementRepository logementRepository) {
        this.logementRepository = logementRepository;
    }

    @Override
    public List<Logement> getAllLogements() {
        return logementRepository.findAll();
    }

    @Override
    public Logement getLogementById(Long id) throws Exception {
        return logementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Logement introuvable avec l'ID : " + id));
    }

    @Override
    public Logement addLogement(Logement logement) throws Exception {
        if (logementRepository.existsByName(logement.getName())) {
            throw new IllegalArgumentException("Le nom du logement existe déjà.");
        }
        return logementRepository.save(logement);
    }

    @Override
    public Logement updateLogement(Long id, Logement logement) throws Exception {
        Logement existing = logementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Logement introuvable avec l'ID : " + id));

        // Vérifier que le nouveau nom (s'il est changé) n'existe pas déjà sur un autre logement
        if (!existing.getName().equals(logement.getName()) && logementRepository.existsByName(logement.getName())) {
            throw new IllegalArgumentException("Le nom du logement existe déjà.");
        }

        existing.setName(logement.getName());
        existing.setPrix(logement.getPrix());
        existing.setRooms(logement.getRooms());

        return logementRepository.save(existing);
    }

    @Override
    public void deleteLogement(Long id) throws Exception {
        Logement existing = logementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Logement introuvable avec l'ID : " + id));
        logementRepository.delete(existing);
    }
}