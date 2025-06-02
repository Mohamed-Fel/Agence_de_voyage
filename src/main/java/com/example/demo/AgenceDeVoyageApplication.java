package com.example.demo;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Categorie;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.entities.Services;
import com.example.demo.repositories.CategorieRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.ServicesRepository;
import com.example.demo.repositories.UserRepository;

@SpringBootApplication
public class AgenceDeVoyageApplication {
	@Autowired
	CategorieRepository categorieRepository;
	
	@Autowired
	ServicesRepository servicesRepository;

	public static void main(String[] args) {
		SpringApplication.run(AgenceDeVoyageApplication.class, args);
	}
	@Bean
	public CommandLineRunner initDefaultData(UserRepository userRepository,
	                                         RoleRepository roleRepository,
	                                         PasswordEncoder passwordEncoder) {
	    return args -> {
	        // ✅ Créer les rôles s'ils n'existent pas
	        Role adminRole = roleRepository.findByName("ADMIN")
	                .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

	        Role agentRole = roleRepository.findByName("AGENT")
	                .orElseGet(() -> roleRepository.save(new Role("AGENT")));

	        // ✅ Créer un admin par défaut s'il n'existe pas
	        String adminEmail = "ahmedahmed@example.com";
	        if (userRepository.findByEmail(adminEmail).isEmpty()) {
	            System.out.println("🛠️ Aucun admin trouvé, création de l'admin par défaut...");

	            // 🔗 Chemin de l’image uploads/1f93d292-c538-4bbe-867b-939e928b4c15_PHOTO.jpg
	            String sourceImagePath = "uploads/96cedda3-d254-4bfd-abbe-469972dbddf9_PHOTO.jpg";

	            String fileName = UUID.randomUUID().toString() + "_" + new File(sourceImagePath).getName();
	            Path destinationPath = Paths.get("uploads").resolve(fileName);
	            Files.createDirectories(destinationPath.getParent());
	            Files.copy(Paths.get(sourceImagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);

	            String imageUrl = "http://localhost:8082/uploads/" + fileName;
	            //Image image = new Image(imageUrl);
	            Image image = new Image();
	            image.setImageURL(imageUrl);

	            Admin admin = new Admin();
	            admin.setEmail(adminEmail);
	            admin.setFirstName("Mohamed");
	            admin.setLastName("Felfel");
	            admin.setUserName("Mohamed_Felfel");
	            admin.setPassword(passwordEncoder.encode("admin1234"));
	            admin.setRole(adminRole); // 🔐 prend le rôle Admin
	            admin.setImage(image);

	            userRepository.save(admin);

	            System.out.println("✅ Admin par défaut créé !");
	        } else {
	            System.out.println("ℹ️ Admin déjà existant.");
	        }
	     // ✅ Ajouter deux catégories par défaut si elles n'existent pas
	        if (categorieRepository.findAll().isEmpty()) {
	            Categorie cat1 = new Categorie();
	            cat1.setName("Hotel");

	            Categorie cat2 = new Categorie();
	            cat2.setName("Maison d'hôte");

	            categorieRepository.save(cat1);
	            categorieRepository.save(cat2);

	            System.out.println("✅ Catégories par défaut créées !");
	        } else {
	            System.out.println("ℹ️ Catégories déjà existantes.");
	        }
	   
        // Ajout des services par défaut
        List<String> defaultServices = List.of(
            "Service en chambre 24h/24",
            "Restauration en chambre",
            "Services de conciergerie",
            "Dépôt de bagages gratuit",
            "Service de blanchisserie et repassage",
            "Service de nettoyage à sec",
            "Voiturier",
            "Service de garde d'enfants",
            "Accessible aux personnes en fauteuil roulant",
            "Ascenseur",
            "Parking gratuit",
            "Bar / Salon",
            "Service de ménage quotidien"
        );

        for (String serviceName : defaultServices) {
            if (servicesRepository.findByName(serviceName).isEmpty()) {
                Services service = new Services();
                service.setName(serviceName);
                servicesRepository.save(service);
            }
        }

        System.out.println("✅ Services par défaut créés !");
    };
	}

}
