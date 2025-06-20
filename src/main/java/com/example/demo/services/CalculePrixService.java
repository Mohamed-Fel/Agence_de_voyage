package com.example.demo.services;

import com.example.demo.entities.ReservationPriceRequest;
import com.example.demo.entities.ReservationPriceResponse;
import com.example.demo.entities.RoomPriceResponse;

public interface CalculePrixService {
    RoomPriceResponse calculerPrixRoom(Long roomId, int nbAdultes, java.util.List<Integer> agesEnfants, Long logementId, int nbNuitees);
    ReservationPriceResponse calculerPrixTotal(ReservationPriceRequest request);

}
