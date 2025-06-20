package com.example.demo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Room;

public interface RoomService {
	
	/*Room addRoom(Room room, Long produitId, List<MultipartFile> imageFiles)throws Exception;*/
	Room saveRoomWithoutLogements(Room room, Long produitId, List<MultipartFile> imageFiles) throws Exception;
	Room getRoomById(Long id);
	List<Room> getRoomsByProduitId(Long produitId);
	void deleteRoom(Long roomId);
	List<Room> getAllRooms();
	Room updateRoom(
	        Long id,
	        String name,
	        int capacite,
	        String description,
	        int nbDeLit,
	        double prixAdulte,
	        double prixEnfant,
	        int ageMinimal,
	        Long produitId,
	        List<Long> logementIds,
	        List<String> logementNames,
	        List<Double> logementPrix,
	        List<MultipartFile> images
	    ) throws Exception;
	
}

