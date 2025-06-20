package com.example.demo.entities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomSelectionRequest {
	  private Long roomId;
	  private int nombreAdultes;
	  private List<Integer> agesEnfants;
	  private Long logementId;
	  private int nbNuitees;
}
