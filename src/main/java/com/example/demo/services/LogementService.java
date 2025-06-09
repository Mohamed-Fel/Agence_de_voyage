package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.Logement;

public interface LogementService {
    List<Logement> getAllLogements();
    Logement getLogementById(Long id) throws Exception;
    Logement addLogement(Logement logement) throws Exception;
    Logement updateLogement(Long id, Logement logement) throws Exception;
    void deleteLogement(Long id) throws Exception;
    List<Logement> getLogementsByRoomId(Long roomId);
    Logement getLogementByRoomIdAndLogementId(Long roomId, Long logementId);
}

