package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Image;
import com.example.demo.entities.Produit;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.ProduitRepository;
import com.example.demo.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private ImageRepository imageRepository;
    
 
    @Override
    public List<Image> getImagesByProduitId(Long produitId) throws Exception {
        List<Image> images = imageRepository.findByProduitId(produitId);
        if (images.isEmpty()) {
            throw new Exception("Aucune image trouvée pour le produit avec id : " + produitId);
        }
        return images;
    }
    @Override
    public List<Image> getImagesByRoomId(Long roomId) throws Exception {
        List<Image> images = imageRepository.findByRoomId(roomId);
        if (images.isEmpty()) {
            throw new Exception("Aucune image trouvée pour la chambre avec id : " + roomId);
        }
        return images;
    }
    @Override
    public void deleteImageById(Long imageId) throws Exception {
        if (!imageRepository.existsById(imageId)) {
            throw new Exception("Image introuvable avec l'id : " + imageId);
        }
        imageRepository.deleteById(imageId);
    }

}
