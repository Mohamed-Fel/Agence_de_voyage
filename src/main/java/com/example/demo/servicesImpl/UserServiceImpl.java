package com.example.demo.servicesImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Agent;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.FileStorageService;
import com.example.demo.services.UserService;

/*import ManageError.CustomException;*/

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final AdminRepository adminRepository;
    @Autowired
    private FileStorageService fileStorageService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository,ImageRepository imageRepository, BCryptPasswordEncoder passwordEncoder ,AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.imageRepository = imageRepository;
        this.adminRepository = adminRepository;
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
        if (agent.getRole() != null && "ADMIN".equalsIgnoreCase(agent.getRole().getName())) {
            Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Le rôle ADMIN n'existe pas dans la base"));

            boolean adminExists = userRepository.existsByRole(adminRole);
            if (adminExists) {
                throw new IllegalArgumentException("Un administrateur existe déjà. Impossible d’en créer un deuxième.");
            }
        }

        // Si image non nulle, sauvegarder l'image d'abord
        Image savedImage = null;
        if (agent.getImage() != null) {
            savedImage = imageRepository.save(agent.getImage());
            agent.setImage(savedImage);
        }

        return userRepository.save(agent);
    }
    @Override
    public Admin editAdminProfile(Long adminId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception {
        Optional<Admin> optionalAdmin = userRepository.findById(adminId).filter(user -> user instanceof Admin).map(user -> (Admin) user);
        if (optionalAdmin.isEmpty()) {
            throw new Exception("Admin not found");
        }

        Admin admin = optionalAdmin.get();

        if (userName != null) admin.setUserName(userName);
        if (firstName != null) admin.setFirstName(firstName);
        if (lastName != null) admin.setLastName(lastName);
        if (email != null) admin.setEmail(email);
        if (password != null && !password.isBlank()) {
            admin.setPassword(passwordEncoder.encode(password));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(imageFile); // méthode pour uploader le fichier et retourner une URL
            updateAdminImage(admin, imageUrl);
        }

        return userRepository.save(admin);
    }
    public Admin updateAdminImage(Admin admin, String imageUrl) {
        Image existingImage = admin.getImage();
        
        if (existingImage != null) {
            // Met à jour l'URL de l'image existante
            existingImage.setImageURL(imageUrl);
        } else {
            // Crée une nouvelle image et l’associe
            Image newImage = new Image();
            newImage.setImageURL(imageUrl);
            admin.setImage(newImage);
        }

        return adminRepository.save(admin);
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