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
    private ImageRepository imageRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private FileStorageService fileStorageService;
    /*public Room saveRoomWithoutLogements(Room room, Long produitId, List<MultipartFile> imageFiles) throws Exception {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + produitId));
        room.setProduit(produit);

        // Sauvegarder les images comme avant
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            String imageUrl = fileStorageService.saveImage(file);
            Image image = new Image();
            image.setImageURL(imageUrl);
            image.setRoom(room);
            images.add(image);
        }
        room.setImages(images);

        // Ici on sauvegarde sans les logements (car room.logements est vide)
        return roomRepository.save(room);
    }*/
    public Room saveRoomWithoutLogements(Room room, Long produitId, List<MultipartFile> imageFiles) throws Exception {
        Produit produit = produitRepository.findById(produitId)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id : " + produitId));
        room.setProduit(produit);

        // Enregistrer la chambre en premier (pour avoir l'ID)
        Room savedRoom = roomRepository.save(room);

        // Enregistrer les images associées
        for (MultipartFile file : imageFiles) {
            String imageUrl = fileStorageService.saveImage(file);
            Image image = new Image();
            image.setImageURL(imageUrl);
            image.setRoom(savedRoom);
            imageRepository.save(image);
        }

        return savedRoom;
    }


    /*@Override
    public Room addRoom(Room room, Long produitId, List<MultipartFile> imageFiles)throws Exception {
        

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
    }*/
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
        List<Long> logementIds,           // IDs des logements existants à rattacher
        List<String> logementNames,       // Noms des nouveaux logements à créer
        List<Double> logementPrix,        // Prix des nouveaux logements à créer
        List<MultipartFile> images
    ) throws Exception {

        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("❌ Chambre non trouvée avec l'ID : " + id));

        Produit produit = produitRepository.findById(produitId)
            .orElseThrow(() -> new RuntimeException("❌ Produit non trouvé avec l'ID : " + produitId));

        // 1. Récupérer les logements existants par leurs IDs (s'ils sont fournis)
        List<Logement> logements = logementIds != null && !logementIds.isEmpty() ?
            logementRepository.findAllById(logementIds) : new ArrayList<>();

        // 2. Créer les nouveaux logements (s'ils sont fournis)
        if (logementNames != null && logementPrix != null && logementNames.size() == logementPrix.size()) {
            for (int i = 0; i < logementNames.size(); i++) {
                Logement newLogement = new Logement();
                newLogement.setName(logementNames.get(i));
                newLogement.setPrix(logementPrix.get(i));
                newLogement.setRoom(room);// Rattacher à la chambre avant sauvegarde (si bidirectionnel)
                logementRepository.save(newLogement);
                logements.add(newLogement); // Ajouter à la liste des logements rattachés à la chambre
            }
        }

        room.setName(name);
        room.setCapacite(capacite);
        room.setDescription(description);
        room.setNbDeLit(nbDeLit);
        room.setPrixAdulte(prixAdulte);
        room.setPrixEnfant(prixEnfant);
        room.setAgeMinimal(ageMinimal);
        room.setProduit(produit);
        
        /*// Mettre à jour la liste des logements (modifiée)
        if (room.getLogements() == null) {
            room.setLogements(new ArrayList<>());
        }
        room.getLogements().clear();
        room.getLogements().addAll(logements);

        // Mettre à jour la relation inverse pour chaque logement
        for (Logement logement : logements) {
            logement.setRoom(room);
        	
        }*/
        // Mettre à jour les logements (relation unidirectionnelle depuis Logement uniquement)
        for (Logement logement : logements) {
            logement.setRoom(room);
            logementRepository.save(logement);
        }


        /*// Supprimer les anciennes images
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
        }*/
        // 🔥 SUPPRIMER les anciennes images associées à cette chambre
        //imageRepository.deleteByRoom(room);
        /*if (room.getImages() != null) {
            room.getImages().clear(); // supprime les anciennes images
        }*/

        // 📸 Ajouter les nouvelles images
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String imageUrl = fileStorageService.saveImage(file);
                Image newImage = new Image();
                newImage.setImageURL(imageUrl);
                newImage.setRoom(room);
                imageRepository.save(newImage);
                //room.getImages().add(newImage);
            }
        }
        return roomRepository.save(room);
    }
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
