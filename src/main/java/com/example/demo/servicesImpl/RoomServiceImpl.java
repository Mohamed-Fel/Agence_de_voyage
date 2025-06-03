package com.example.demo.servicesImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Image;
import com.example.demo.entities.Logement;
import com.example.demo.entities.Produit;
import com.example.demo.entities.Room;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.LogementRepository;
import com.example.demo.repositories.ProduitRepository;
import com.example.demo.repositories.RoomRepository;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private LogementRepository logementRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Room addRoom(Room room, List<Long> logementIds, Long produitId, List<MultipartFile> imageFiles)throws Exception {
        // 🔗 Récupérer les logements
        List<Logement> logements = logementRepository.findAllById(logementIds);
        room.setLogements(logements);

        // 🏨 Associer le produit
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + produitId));
        room.setProduit(produit);

        // 🖼️ Sauvegarder les images
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            String imageUrl = fileStorageService.saveImage(file);
            Image image = new Image();
            image.setImageURL(imageUrl);
            image.setRoom(room); // Association bidirectionnelle
            images.add(image);
        }
        room.setImages(images);

        // 💾 Sauvegarder la room avec cascade sur les images
        return roomRepository.save(room);
    }
    @Override
    public Room getRoomById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide. L'ID doit être positif.");
        }

        return roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aucune chambre trouvée avec l'ID : " + id));
    }
    @Override
    public List<Room> getRoomsByProduitId(Long produitId) {
        if (produitId == null || produitId <= 0) {
            throw new IllegalArgumentException("❌ L'ID du produit est invalide.");
        }

        return roomRepository.findByProduit_Id(produitId);
    }
    @Override
    public void deleteRoom(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new IllegalArgumentException("❌ L'ID de la chambre est invalide.");
        }

        Optional<Room> roomOptional = roomRepository.findById(roomId);

        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("❌ Aucune chambre trouvée avec l'ID : " + roomId);
        }

        roomRepository.deleteById(roomId);
    }
    @Override
    public Room updateRoom(
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
        List<MultipartFile> images
    ) throws Exception {

        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("❌ Chambre non trouvée avec l'ID : " + id));

        Produit produit = produitRepository.findById(produitId)
            .orElseThrow(() -> new RuntimeException("❌ Produit non trouvé avec l'ID : " + produitId));

        List<Logement> logements = logementIds != null ?
            logementRepository.findAllById(logementIds) : new ArrayList<>();

        room.setName(name);
        room.setCapacite(capacite);
        room.setDescription(description);
        room.setNbDeLit(nbDeLit);
        room.setPrixAdulte(prixAdulte);
        room.setPrixEnfant(prixEnfant);
        room.setAgeMinimal(ageMinimal);
        room.setProduit(produit);
        room.setLogements(logements);

        // Supprimer les anciennes images
        if (room.getImages() != null) {
            room.getImages().clear();
        }

        // Ajouter les nouvelles images
        if (images != null) {
            // Supprimer proprement les anciennes images
            List<Image> existingImages = room.getImages();
            if (existingImages != null) {
                existingImages.clear(); // ça déclenche orphanRemoval
            }

            for (MultipartFile file : images) {
                String imageUrl = fileStorageService.saveImage(file);
                Image newImage = new Image();
                newImage.setImageURL(imageUrl);
                newImage.setRoom(room);
                room.getImages().add(newImage); // ne pas remplacer la liste, juste ajouter
            }
        }

        return roomRepository.save(room);
    }
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
