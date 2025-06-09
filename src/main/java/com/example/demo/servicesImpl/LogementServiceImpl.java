package com.example.demo.servicesImpl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Logement;
import com.example.demo.repositories.LogementRepository;
import com.example.demo.repositories.RoomRepository;
import com.example.demo.services.LogementService;

@Service
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;
    @Autowired
    private RoomRepository roomRepository;

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
        /*if (logementRepository.existsByName(logement.getName())) {
            throw new IllegalArgumentException("Le nom du logement existe déjà.");
        }*/
        return logementRepository.save(logement);
    }

    @Override
    public Logement updateLogement(Long id, Logement logement) throws Exception {
        Logement existing = logementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Logement introuvable avec l'ID : " + id));

        // Vérifier que le nouveau nom (s'il est changé) n'existe pas déjà sur un autre logement
        if (logement.getName() != null && !logement.getName().equals(existing.getName())
                && logementRepository.existsByName(logement.getName())) {
            throw new IllegalArgumentException("Le nom du logement existe déjà.");
        }

        // Mise à jour partielle uniquement des champs non null reçus
        if (logement.getName() != null) {
            existing.setName(logement.getName());
        }

        if (logement.getPrix() != null) {
            existing.setPrix(logement.getPrix());
        }

        return logementRepository.save(existing);
    }

    @Override
    public void deleteLogement(Long id) throws Exception {
        Logement existing = logementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Logement introuvable avec l'ID : " + id));
        logementRepository.delete(existing);
    }
    @Override
    public List<Logement> getLogementsByRoomId(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new NoSuchElementException("❌ Chambre avec ID " + roomId + " n'existe pas.");
        }
        return logementRepository.findByRoom_Id(roomId);
    }
    @Override
    public Logement getLogementByRoomIdAndLogementId(Long roomId, Long logementId) {
        if (!roomRepository.existsById(roomId)) {
            throw new NoSuchElementException("❌ La chambre avec l'ID " + roomId + " n'existe pas.");
        }

        return logementRepository.findByIdAndRoom_Id(logementId, roomId)
            .orElseThrow(() -> new NoSuchElementException(
                "❌ Aucun logement avec ID " + logementId + " associé à la chambre " + roomId));
    }
}