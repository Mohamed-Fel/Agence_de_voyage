package com.example.demo.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Logement;
import com.example.demo.entities.ReservationPriceRequest;
import com.example.demo.entities.ReservationPriceResponse;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomPriceResponse;
import com.example.demo.entities.RoomSelectionRequest;
import com.example.demo.repositories.LogementRepository;
import com.example.demo.repositories.RoomRepository;
import com.example.demo.services.CalculePrixService;
@Service
public class CalculePrixServiceImpl implements CalculePrixService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LogementRepository logementRepository;
    @Override
    public RoomPriceResponse calculerPrixRoom(Long roomId, int nbAdultes, List<Integer> agesEnfants, Long logementId, int nbNuitees) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found"));

        Logement logement = null;
        if (logementId != null) {
            logement = logementRepository.findById(logementId)
                .orElse(null);
        }

        double total = 0;

        // Prix pour adultes
        total += nbAdultes * room.getPrixAdulte() * nbNuitees;

        int ageMinimal = room.getAgeMinimal(); // âge minimal dynamique

        // Compter le nombre d'enfants payants
        int nombreEnfantsPayants = 0;
        for (Integer age : agesEnfants) {
            if (age >= ageMinimal) {
                total += room.getPrixEnfant() * nbNuitees;  // prix enfant seulement si age >= ageMinimal
                nombreEnfantsPayants++;
            }
            // enfant < ageMinimal est gratuit, donc pas de prix ajouté
        }

        // Prix logement : multiplier par adultes + enfants payants uniquement
        if (logement != null) {
            total += logement.getPrix() * (nbAdultes + nombreEnfantsPayants) * nbNuitees;
        }

        RoomPriceResponse response = new RoomPriceResponse();
        response.setRoomId(roomId);
        response.setPrixTotalRoom(total);
        return response;
    }
    @Override
    public ReservationPriceResponse calculerPrixTotal(ReservationPriceRequest request) {
        List<RoomSelectionRequest> selections = request.getSelections();
        List<RoomPriceResponse> details = new ArrayList<>();
        double total = 0;

        for (RoomSelectionRequest req : selections) {
            RoomPriceResponse roomPrice = calculerPrixRoom(
                req.getRoomId(),
                req.getNombreAdultes(),
                req.getAgesEnfants(),
                req.getLogementId(),
                req.getNbNuitees()
            );
            total += roomPrice.getPrixTotalRoom();
            details.add(roomPrice);
        }

        ReservationPriceResponse resp = new ReservationPriceResponse();
        resp.setRoomPrices(details);
        resp.setPrixTotalReservation(total);
        return resp;
    }
}
