package com.example.demo.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPriceResponse {
    private List<RoomPriceResponse> roomPrices;
    private double prixTotalReservation;
}
