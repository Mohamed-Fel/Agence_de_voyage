package com.example.demo.controlleurs;

import com.example.demo.entities.Agent;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.UserService;

import java.io.IOException;
import org.springframework.http.MediaType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final RoleRepository  roleRepository;
    
    private static final String rootLocation = "uploads/";

    public UserController(UserService userService ,FileStorageService fileStorageService,RoleRepository  roleRepository ) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.roleRepository= roleRepository;
    }

    /*@PostMapping("/agent")
    public ResponseEntity<?> createAgent(@RequestBody Agent agent) {
        if (agent.getUserName() == null || agent.getUserName().length() <= 2) {
            throw new IllegalArgumentException("Username is too short (minimum 3 characters)");
        }
        Agent saved = userService.createAgent(agent);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    /*@PostMapping(value = "/agent", consumes = "multipart/form-data")
    public ResponseEntity<Agent> createAgent(
            @RequestPart("agent") Agent agent,
            @RequestPart("imageFile") MultipartFile imageFile) {

        if (!imageFile.isEmpty()) {
            try {
                // Créer le dossier s'il n'existe pas
                File uploadDir = new File(rootLocation);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Nom unique de fichier
                String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(rootLocation, filename);

                // Sauvegarder le fichier sur le disque
                Files.write(filePath, imageFile.getBytes());

                // Créer l'objet Image
                Image image = new Image("/" + rootLocation + filename);

                // Assigner l'image à l'agent
                agent.setImage(image);
                

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().build();
            }
        }

        Agent savedAgent = userService.createAgent(agent);
        return ResponseEntity.ok(savedAgent);
    }*/
    /*@PostMapping("/agent")
    public ResponseEntity<?> createAgent(@RequestBody Agent agent) {
    	  try {
    	        Agent savedAgent = userService.createAgent(agent);
    	        
    	        
    	        // Créer une réponse personnalisée
    	        Map<String, Object> response = new HashMap<>();
    	        response.put("message", "✅ Agent créé avec succès.");
    	        response.put("agent", savedAgent);
    	        
    	        return ResponseEntity.ok(response);
    	        
    	    } catch (Exception e) {
    	        Map<String, String> error = new HashMap<>();
    	        error.put("error", "❌ Erreur lors de la création : " + e.getMessage());
    	        return ResponseEntity.badRequest().body(error);
    	    }
    }*/
    @PostMapping(value = "/agent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAgentWithImage(
            @RequestParam("userName") String userName,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("roleId") Long roleId,
            @RequestParam("image") MultipartFile imageFile
    ) {
        try {
            // 🔐 Sauvegarder l'image
            String imageUrl = fileStorageService.saveImage(imageFile);

            // 📌 Créer l'entité Image
            Image image = new Image(imageUrl);

            // 🎯 Récupérer le rôle
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Rôle introuvable avec l'id : " + roleId));

            // 👤 Créer l'objet Agent
            Agent agent = new Agent(userName, firstName, lastName, email, password, image, role);

            // 💾 Sauvegarder l'agent
            Agent savedAgent = userService.createAgent(agent);
            savedAgent.setPassword(null); // on ne retourne pas le mot de passe

            // 📦 Construire la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ Agent créé avec succès.");
            response.put("agent", savedAgent);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "❌ Erreur lors de la création : " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    
    
}
