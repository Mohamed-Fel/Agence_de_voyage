package com.example.demo.servicesImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Agent;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

/*import ManageError.CustomException;*/

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,ImageRepository imageRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Agent createAgent(Agent agent) throws Exception {
        // Vérifie si l'email existe déjà
        if (userRepository.existsByEmail(agent.getEmail())) {
            throw new Exception("L'email est déjà utilisé.");
        }

        if (agent.getEmail() == null || agent.getEmail().isEmpty()) {
            throw new Exception("Email est obligatoire.");
        }

        if (agent.getPassword() == null || agent.getPassword().length() < 6) {
            throw new Exception("Le mot de passe doit contenir au moins 6 caractères.");
        }
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
     // Charger le rôle depuis la base
        Role role = roleRepository.findById(agent.getRole().getId())
            .orElseThrow(() -> new IllegalArgumentException("Rôle non trouvé avec ID : " + agent.getRole().getId()));

        agent.setRole(role);
        

        if (agent.getRole() == null || roleRepository.findById(agent.getRole().getId()).isEmpty()) {
            throw new Exception("Rôle invalide.");
        }

        // Si image non nulle, sauvegarder l'image d'abord
        Image savedImage = null;
        if (agent.getImage() != null) {
            savedImage = imageRepository.save(agent.getImage());
            agent.setImage(savedImage);
        }

        return userRepository.save(agent);
    }

    /*@Override
    public Agent createAgent(Agent agent) {
    	
    	   // Étape 1: récupérer le rôle depuis la base
        Role role = roleRepository.findById(agent.getRole().getId())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // Étape 2: vérifier que c’est bien le rôle AGENT
        if (!"AGENT".equalsIgnoreCase(role.getName())) {
            throw new IllegalArgumentException("Invalid role: Only AGENT role is allowed");
        }
    	
        if (agent.getRole() != null && "ADMIN".equalsIgnoreCase(agent.getRole().getName())) {
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new CustomException("Le rôle ADMIN n'existe pas dans la base"));

            boolean adminExists = userRepository.existsByRole(adminRole);
            if (adminExists) {
                throw new CustomException("Un administrateur existe déjà. Impossible d’en créer un deuxième.");
            }
        }
    	
        // Crypter le mot de passe
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        return userRepository.save(agent);
    }*/
   
    

}