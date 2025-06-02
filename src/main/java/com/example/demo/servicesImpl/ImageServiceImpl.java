package com.example.demo.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Image;
import com.example.demo.entities.Produit;
import com.example.demo.repositories.ProduitRepository;
import com.example.demo.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService{
    @Autowired
    private ProduitRepository produitRepository;
    
    /*@Override
    public List<Image> getImagesByProduitId(Long produitId) throws Exception {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new Exception("Produit non trouvé avec id : " + produitId));
        return produit.getImages();
    }*/
    @Override
    public List<Image> getImagesByProduitId(Long produitId) throws Exception {
        Produit produit = produitRepository.findByIdWithImages(produitId)
                .orElseThrow(() -> new Exception("Produit non trouvé avec id : " + produitId));
        return produit.getImages();
    }

}
