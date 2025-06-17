package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Image;
import com.example.demo.entities.Produit;
import com.example.demo.entities.Room;

public interface ImageRepository extends JpaRepository<Image, Long>{
	List<Image> findByProduitId(Long produitId);
	void deleteByRoom(Room room);
	void deleteByProduit(Produit produit);
	List<Image> findByRoomId(Long roomId);
	void deleteById(Long id);

}
