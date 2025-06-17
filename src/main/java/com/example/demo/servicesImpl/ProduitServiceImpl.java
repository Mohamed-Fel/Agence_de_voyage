package com.example.demo.servicesImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.example.demo.entities.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Categorie;
import com.example.demo.entities.Contrat;
import com.example.demo.entities.Image;
import com.example.demo.entities.Localisation;
import com.example.demo.entities.Produit;
import com.example.demo.entities.User;
import com.example.demo.repositories.CategorieRepository;
import com.example.demo.repositories.ContratRepository;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.ProduitRepository;
import com.example.demo.repositories.ServicesRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.ProduitService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final ServicesRepository servicesRepository;
    private final ContratRepository contratRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    @Autowired
    ImageRepository imageRepository;

    @Override
    public Produit createProduit(
        String name, String description, String phoneNumber, String email, int nbEtoiles,
        String pays, String ville, String adresse, int initialPrix, Long categorieId,
        Long contratId, List<Long> serviceIds, List<MultipartFile> images, Long creatorId ,Localisation localisation
    ) throws Exception {
    	// ✅ Validation du numéro
        if (!phoneNumber.matches("^\\+\\d{1,3}\\d{6,10}$")) {
            throw new IllegalArgumentException("❌ Numéro de téléphone invalide. Format attendu : +21612345678");
        }

        Produit produit = new Produit();
        produit.setName(name);
        produit.setDescription(description);
        produit.setPhoneNumber(phoneNumber);
        produit.setEmail(email);
        produit.setNbEtoiles(nbEtoiles);
        produit.setPays(pays);
        produit.setVille(ville);
        produit.setAdresse(adresse);
        produit.setInitialPrix(initialPrix);
        produit.setLocalisation(localisation);

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new Exception("Créateur non trouvé"));
        produit.setCreator(creator);

        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new Exception("Catégorie non trouvée"));
        produit.setCategorie(categorie);

        Contrat contrat = contratRepository.findById(contratId)
                .orElseThrow(() -> new Exception("Contrat non trouvé"));
        produit.setContrat(contrat);

        List<Services> services = servicesRepository.findAllById(serviceIds);
        if (services.size() != serviceIds.size()) {
            throw new Exception("Un ou plusieurs services sont invalides");
        }
        produit.setServices(services);

        Produit savedProduit = produitRepository.save(produit);

        List<Image> imageEntities = new ArrayList<>();
        /*for (MultipartFile file : images) {
            String imageUrl = fileStorageService.saveImage(file);
            Image image = new Image();
            image.setImageURL(imageUrl);
            image.setProduit(savedProduit);
            imageEntities.add(image);
        }*/
        
        // Sauvegarder les images associées
        for (MultipartFile file : images) {
            String imageUrl = fileStorageService.saveImage(file);
            Image image = new Image();
            image.setImageURL(imageUrl);
            image.setProduit(savedProduit);
            imageRepository.save(image);
        }
        savedProduit.setImages(imageEntities);


        return savedProduit;
    }
    
    @Override
    @Transactional
    public Produit updateProduit(
        Long id, String name, String description, String phoneNumber, String email, int nbEtoiles,
        String pays, String ville, String adresse, int initialPrix, Long categorieId,
        Long contratId, List<Long> serviceIds, List<MultipartFile> images, Long creatorId ,Localisation localisation
    ) throws Exception {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new Exception("Produit non trouvé"));

        produit.setName(name);
        produit.setDescription(description);
        produit.setPhoneNumber(phoneNumber);
        produit.setEmail(email);
        produit.setNbEtoiles(nbEtoiles);
        produit.setPays(pays);
        produit.setVille(ville);
        produit.setAdresse(adresse);
        produit.setInitialPrix(initialPrix);
        produit.setLocalisation(localisation);
        if (!phoneNumber.matches("^\\+\\d{1,3}\\d{6,10}$")) {
            throw new IllegalArgumentException("❌ Numéro invalide. Exemple : +21612345678");
        }

        if (categorieId != null) {
            Categorie categorie = categorieRepository.findById(categorieId)
                    .orElseThrow(() -> new Exception("Catégorie non trouvée"));
            produit.setCategorie(categorie);
        }

        if (contratId != null) {
            Contrat contrat = contratRepository.findById(contratId)
                    .orElseThrow(() -> new Exception("Contrat non trouvé"));
            produit.setContrat(contrat);
        }

        if (creatorId != null) {
            User creator = userRepository.findById(creatorId)
                    .orElseThrow(() -> new Exception("Créateur non trouvé"));
            produit.setCreator(creator);
        }

        if (serviceIds != null) {
            List<Services> services = servicesRepository.findAllById(serviceIds);
            produit.setServices(services);
        }

        /*if (images != null && !images.isEmpty()) {
            List<Image> imageEntities = new ArrayList<>();
            for (MultipartFile file : images) {
                String imageUrl = fileStorageService.saveImage(file);
                Image image = new Image();
                image.setImageURL(imageUrl);
                image.setProduit(produit);
                imageEntities.add(image);
            }
            produit.getImages().clear();
            produit.getImages().addAll(imageEntities);
        }*/
        // Supprimer les anciennes images liées à ce produit
        imageRepository.deleteByProduit(produit);

        // Ajouter les nouvelles images si fournies
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String imageUrl = fileStorageService.saveImage(file);
                Image image = new Image();
                image.setImageURL(imageUrl);
                image.setProduit(produit);
                imageRepository.save(image);
            }
        }

        return produitRepository.save(produit);
    }
    @Override
    public List<Produit> getProduitsByCategorieName(String nomCategorie) {
        return produitRepository.findByCategorieName(nomCategorie);
    }
    @Override
    public void deleteProduit(Long id) {
        Produit produit = produitRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Produit introuvable avec l'ID : " + id));
      
        produitRepository.delete(produit);
    }
    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }
}
