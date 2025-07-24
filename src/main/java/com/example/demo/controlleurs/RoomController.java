package com.example.demo.controlleurs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Localisation;
import com.example.demo.entities.Logement;
import com.example.demo.entities.Room;
import com.example.demo.repositories.LogementRepository;
import com.example.demo.repositories.RoomRepository;
import com.example.demo.services.RoomService;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rooms")
public class RoomController {
	@Autowired
    private RoomService roomService;
	@Autowired
	private LogementRepository logementRepository;
	@Autowired
	private RoomRepository roomRepository;
	

    /*@PostMapping(value = "/add", consumes = "multipart/form-data")
    public ResponseEntity<?> addRoom(
            @RequestParam("name") String name,
            @RequestParam("capacite") int capacite,
            @RequestParam("description") String description,
            @RequestParam("nbDeLit") int nbDeLit,
            @RequestParam("prixAdulte") double prixAdulte,
            @RequestParam("prixEnfant") double prixEnfant,
            @RequestParam("ageMinimal") int ageMinimal,
            //@RequestParam("logementIds") List<Long> logementIds,
            @RequestParam("logementNames") List<String> logementNames,
            @RequestParam("logementPrix") List<Double> logementPrix,
            @RequestParam("produitId") Long produitId,
            @RequestParam("images") List<MultipartFile> imageFiles
           
            
            
    ) {
        try {
        	
            Room room = new Room();
            room.setName(name);
            room.setCapacite(capacite);
            room.setDescription(description);
            room.setNbDeLit(nbDeLit);
            room.setPrixAdulte(prixAdulte);
            room.setPrixEnfant(prixEnfant);
            room.setAgeMinimal(ageMinimal);
            //room.setLogement(logement);
            // Créer les logements à partir des paramètres
            List<Logement> logements = new ArrayList<>();
            for (int i = 0; i < logementNames.size(); i++) {
                Logement logement = new Logement();
                logement.setName(logementNames.get(i));
                logement.setPrix(logementPrix.get(i));
                logement.setRoom(room); // très important pour cascade
                logements.add(logement);
            }
            room.setLogements(logements);

            Room savedRoom = roomService.addRoom(room, produitId, imageFiles);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambre ajoutée avec succès.");
            response.put("room", savedRoom);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "❌ Erreur lors de l'ajout de la chambre : " + e.getMessage())
            );
        }
    }*/
	@PostMapping(value = "/add", consumes = "multipart/form-data") 
	// Méthode HTTP POST exposée sur l'endpoint /add qui accepte des données de type multipart/form-data (pour gérer les fichiers)
	public ResponseEntity<?> addRoom(
	        @RequestParam("name") String name,                  // Nom de la chambre envoyé par le formulaire
	        @RequestParam("capacite") int capacite,             // Capacité de la chambre
	        @RequestParam("description") String description,    // Description de la chambre
	        @RequestParam("nbDeLit") int nbDeLit,               // Nombre de lits dans la chambre
	        @RequestParam("prixAdulte") double prixAdulte,      // Prix adulte
	        @RequestParam("prixEnfant") double prixEnfant,      // Prix enfant
	        @RequestParam("ageMinimal") int ageMinimal,         // Âge minimal pour la chambre
	        @RequestParam("logementNames") List<String> logementNames, // Liste des noms des logements associés (peut être plusieurs)
	        @RequestParam("logementPrix") List<Double> logementPrix,   // Liste des prix correspondants aux logements
	        @RequestParam("produitId") Long produitId,          // ID du produit auquel la chambre est associée
	        @RequestParam("images") List<MultipartFile> imageFiles      // Liste des fichiers images uploadés
	) {
	    try {
	        // ✅ Vérifier l'unicité du nom de la chambre
	        /*Optional<Room> existingRoom = roomRepository.findByName(name);
	        if (existingRoom.isPresent()) {
	            return ResponseEntity.badRequest().body(
	                Map.of("error", "❌ Une chambre avec ce nom existe déjà.")
	            );
	        }*/
	        // Création d'une instance Room et assignation des valeurs reçues
	        Room room = new Room();
	        room.setName(name);
	        room.setCapacite(capacite);
	        room.setDescription(description);
	        room.setNbDeLit(nbDeLit);
	        room.setPrixAdulte(prixAdulte);
	        room.setPrixEnfant(prixEnfant);
	        room.setAgeMinimal(ageMinimal);

	        // Sauvegarder la Room dans la base (sans les logements), pour obtenir un id et pouvoir y associer les logements
	        Room savedRoom = roomService.saveRoomWithoutLogements(room, produitId, imageFiles);
	        System.out.println("✅ Room sauvegardée : " + savedRoom);

	        // Préparer la liste des logements à associer à cette Room
	        List<Logement> logements = new ArrayList<>();

	        // Parcours des listes logementNames et logementPrix pour créer ou récupérer les logements existants
	        for (int i = 0; i < logementNames.size(); i++) {
	            String logName = logementNames.get(i);    // Nom du logement i
	            Double logPrix = logementPrix.get(i);    // Prix du logement i

	            // Recherche en base d'un logement avec ce nom et ce prix
	            List<Logement> existingLogementOpt = logementRepository.findByNameAndPrix(logName, logPrix);

	            /*Logement logement;
	            if (existingLogementOpt.isPresent()) {
	                // Si un logement existe déjà avec ce nom et prix,
	                // on récupère ce logement existant
	                logement = existingLogementOpt.get();
	                // Puis on modifie son association pour le rattacher à la nouvelle Room créée
	                logement.setRoom(savedRoom);
	                
	            } else {
	                // Sinon, on crée un nouveau logement avec les infos reçues
	                logement = new Logement();
	                logement.setName(logName);
	                logement.setPrix(logPrix);
	                // On l'associe à la Room nouvellement créée
	                logement.setRoom(savedRoom);
	                
	            }
	            // On ajoute ce logement (existant ou nouveau) à la liste à sauvegarder
	            logements.add(logement);*/
	            Logement logementToUse;

	            if (!existingLogementOpt.isEmpty()) {
	                // Cherche un logement non encore associé à une room
	                Optional<Logement> unassigned = existingLogementOpt.stream()
	                        .filter(log -> log.getRoom() == null)
	                        .findFirst();

	                if (unassigned.isPresent()) {
	                    logementToUse = unassigned.get();
	                    logementToUse.setRoom(savedRoom);
	                    logements.add(logementToUse);
	                } else {
	                    // Tous les logements sont déjà associés, on en crée un nouveau
	                    Logement newLogement = new Logement();
	                    newLogement.setName(logName);
	                    newLogement.setPrix(logPrix);
	                    newLogement.setRoom(savedRoom);
	                    logements.add(newLogement);
	                }
	            } else {
	                // Aucun logement trouvé, on en crée un nouveau
	                Logement newLogement = new Logement();
	                newLogement.setName(logName);
	                newLogement.setPrix(logPrix);
	                newLogement.setRoom(savedRoom);
	                logements.add(newLogement);
	            }
	        }

	        // Sauvegarder en base tous les logements (ceux modifiés et ceux créés)
	        logementRepository.saveAll(logements);

	        // Optionnel : Mettre à jour la liste de logements de la Room sauvegardée pour refléter les changements
	        //savedRoom.setLogements(logements);

	        // Préparer la réponse HTTP contenant un message succès et la Room créée
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "✅ Chambre ajoutée avec succès, logements associés mis à jour.");
	        response.put("room", savedRoom);

	        // Retourner un code 201 Created avec la réponse
	        return ResponseEntity.status(HttpStatus.CREATED).body(response);

	    } catch (Exception e) {
	        // En cas d'erreur, renvoyer un code 400 Bad Request avec un message d'erreur détaillé
	        return ResponseEntity.badRequest().body(
	                Map.of("error", "❌ Erreur lors de l'ajout de la chambre : " + e.getMessage())
	        );
	    }
	}
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);

            return ResponseEntity.ok(
                Map.of(
                    "message", "✅ Chambre trouvée avec succès.",
                    "room", room
                )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "❌ " + e.getMessage())
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(
                Map.of("error", "❌ " + e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                Map.of("error", "❌ Une erreur inattendue est survenue : " + e.getMessage())
            );
        }
    }
    @GetMapping("/by-produit")
    public ResponseEntity<?> getRoomsByProduitId(@RequestParam("produitId") Long produitId) {
        try {
            if (produitId == null || produitId <= 0) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "❌ L'ID du produit est invalide.")
                );
            }

            List<Room> rooms = roomService.getRoomsByProduitId(produitId);

            if (rooms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("error", "❌ Aucune chambre trouvée pour le produit ID : " + produitId)
                );
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambres trouvées avec succès.");
            response.put("rooms", rooms);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "❌ Erreur interne : " + e.getMessage())
            );
        }
    }
    @GetMapping("/available-rooms")
    public ResponseEntity<?> getAvailableRooms(
        @RequestParam Long produitId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkIn,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOut) {

        try {
            if (checkIn.isAfter(checkOut)) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "❌ La date de check-in doit être avant la date de check-out.")
                );
            }

            List<Room> availableRooms = roomService.getAvailableRooms(produitId, checkIn, checkOut);

            if (availableRooms.isEmpty()) {
                return ResponseEntity.ok(
                    Map.of("message", "❌ Aucune chambre disponible pour cette période.")
                );
            }

            return ResponseEntity.ok(
                Map.of("message", "✅ Chambres disponibles trouvées", "rooms", availableRooms)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "❌ Erreur interne : " + e.getMessage())
            );
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.ok(
                    Map.of("message", "✅ Chambre supprimée avec succès.")
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "❌ Une erreur est survenue : " + e.getMessage())
            );
        }
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoom(
        @PathVariable Long id,
        @RequestParam String name,
        @RequestParam int capacite,
        @RequestParam String description,
        @RequestParam int nbDeLit,
        @RequestParam double prixAdulte,
        @RequestParam double prixEnfant,
        @RequestParam int ageMinimal,
        @RequestParam Long produitId,
        @RequestParam(required = false) List<Long> logementIds,
        @RequestParam(required = false) List<String> logementNames,
        @RequestParam(required = false) List<Double> logementPrix,
        @RequestParam(required = false) List<MultipartFile> images
    ) {
        try {
            Room updatedRoom = roomService.updateRoom(
                id, name, capacite, description, nbDeLit, prixAdulte, prixEnfant,
                ageMinimal, produitId, logementIds, logementNames, logementPrix, images
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Chambre mise à jour avec succès.");
            response.put("room", updatedRoom);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur serveur lors de la mise à jour : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            if (rooms.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "ℹ️ Aucune chambre trouvée.");
                return ResponseEntity.ok(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Liste des chambres récupérée avec succès.");
            response.put("rooms", rooms);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la récupération des chambres : " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}


