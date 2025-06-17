package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Image;

public interface ImageService {
	List<Image> getImagesByProduitId(Long produitId) throws Exception;
	List<Image> getImagesByRoomId(Long roomId) throws Exception;
	void deleteImageById(Long imageId) throws Exception;

}
