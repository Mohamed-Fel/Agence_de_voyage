package com.example.demo.servicesImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Agent;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.AgentRepository;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtService;
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
    AgentRepository agentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private JwtService jwtService;
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
    
    @Override
    public Agent editAgentProfile(Long agentId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception {
        Optional<Agent> optionalAgent = userRepository.findById(agentId).filter(user -> user instanceof Agent).map(user -> (Agent) user);
        if (optionalAgent.isEmpty()) {
            throw new Exception("Agent non trouvé");
        }

        Agent agent = optionalAgent.get();

        if (userName != null) agent.setUserName(userName);
        if (firstName != null) agent.setFirstName(firstName);
        if (lastName != null) agent.setLastName(lastName);
        if (email != null) agent.setEmail(email);
        if (password != null && !password.isBlank()) {
            agent.setPassword(passwordEncoder.encode(password));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(imageFile);
            updateAgentImage(agent, imageUrl);
        }

        return userRepository.save(agent);
    }

    public Agent updateAgentImage(Agent agent, String imageUrl) {
        Image existingImage = agent.getImage();

        if (existingImage != null) {
            existingImage.setImageURL(imageUrl);
        } else {
            Image newImage = new Image();
            newImage.setImageURL(imageUrl);
            agent.setImage(newImage);
        }

        return agentRepository.save(agent);
    }
    /*@Override
    public User editUserProfile(Long userId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé avec l'ID : " + userId));

        if (userName != null) user.setUserName(userName);
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (email != null) user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(imageFile);
            updateUserImage(user, imageUrl);
        }

        return userRepository.save(user);
    }*/
    
    @Override
    public Map<String, Object> editUserProfile(Long userId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé avec l'ID : " + userId));

        boolean emailChanged = (email != null && !email.equals(user.getEmail()));
        boolean passwordChanged = (password != null && !password.isBlank());

        if (userName != null) user.setUserName(userName);
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (emailChanged) user.setEmail(email);
        if (passwordChanged) user.setPassword(passwordEncoder.encode(password));

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.saveImage(imageFile);
            updateUserImage(user, imageUrl);
        }

        user = userRepository.save(user);

        // ✅ Générer un nouveau token si email ou password ont changé
        String newToken = null;
        if (emailChanged || passwordChanged) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName()));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user.getEmail(), null, authorities
            );
            newToken = jwtService.generateToken(authentication);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("updatedUser", user);
        result.put("newToken", newToken);

        return result;
    }
    public void updateUserImage(User user, String imageUrl) {
        Image existingImage = user.getImage();

        if (existingImage != null) {
            existingImage.setImageURL(imageUrl);
        } else {
            Image newImage = new Image();
            newImage.setImageURL(imageUrl);
            user.setImage(newImage);
        }
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