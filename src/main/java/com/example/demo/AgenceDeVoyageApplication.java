package com.example.demo;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Image;
import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

@SpringBootApplication
public class AgenceDeVoyageApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenceDeVoyageApplication.class, args);
	}
	/*@Bean
	public CommandLineRunner createDefaultAdmin(UserRepository userRepository,
	                                            RoleRepository roleRepository,
	                                            PasswordEncoder passwordEncoder) {
	    return args -> {
	        String adminEmail = "ahmedahmed@example.com";

	        if (userRepository.findByEmail(adminEmail).isEmpty()) {
	            System.out.println("🛠️ Aucun admin trouvé, création d'un utilisateur admin par défaut...");

	            // Vérifie si le rôle "ADMIN" existe, sinon le crée
	            Role adminRole = roleRepository.findByName("Admin")
	                    .orElseGet(() -> {
	                        Role newRole = new Role();
	                        newRole.setName("Admin");
	                        return roleRepository.save(newRole);
	                    });

	            Admin admin = new Admin();
	            admin.setEmail(adminEmail);
	            admin.setFirstName("Admin");
	            admin.setLastName("NoImage");
	            admin.setUserName("admin_user");
	            admin.setPassword(passwordEncoder.encode("admin1234")); // mot de passe en clair, sera haché
	            admin.setRole(adminRole);

	            userRepository.save(admin);

	            System.out.println("✅ Utilisateur admin créé avec succès (email: " + adminEmail + ", mot de passe: admin123)");
	        } else {
	            System.out.println("ℹ️ Un utilisateur admin existe déjà avec l'email " + adminEmail);
	        }
	    };
	}*/
	/*@Bean
	public CommandLineRunner createDefaultAdmin(UserRepository userRepository,
	                                            RoleRepository roleRepository,
	                                            PasswordEncoder passwordEncoder) {
	    return args -> {
	        String adminEmail = "ahmedahmed@example.com";

	        if (userRepository.findByEmail(adminEmail).isEmpty()) {
	            System.out.println("🛠️ Aucun admin trouvé, création d'un utilisateur admin par défaut...");

	            // Vérifie si le rôle "ADMIN" existe, sinon le crée
	            Role adminRole = roleRepository.findByName("Admin")
	                    .orElseGet(() -> {
	                        Role newRole = new Role();
	                        newRole.setName("Admin");
	                        return roleRepository.save(newRole);
	                    });

	            String sourceImagePath = "C:\\Users\\felfe\\OneDrive\\Documents\\Images\\PHOTO.jpg";
	            String fileName = UUID.randomUUID().toString() + "_" + new File(sourceImagePath).getName();
	            Path destinationPath = Paths.get("C:/Users/felfe/Documents/uploads").resolve(fileName);
	            Files.createDirectories(destinationPath.getParent());
	            Files.copy(Paths.get(sourceImagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);

	            String imageUrl = "http://localhost:8082/uploads/" + fileName;
	            Image image = new Image(imageUrl);
	            

	            // 👤 Créer l'admin
	            Admin admin = new Admin();
	            admin.setEmail(adminEmail);
	            admin.setFirstName("Admin");
	            admin.setLastName("NoImage");
	            admin.setUserName("admin_user");
	            admin.setPassword(passwordEncoder.encode("admin1234"));
	            admin.setRole(adminRole);
	            admin.setImage(image);

	            userRepository.save(admin);

	            System.out.println("✅ Utilisateur admin créé avec succès !");
	            System.out.println("📷 Image URL : " + imageUrl);
	        } else {
	            System.out.println("ℹ️ Un utilisateur admin existe déjà avec l'email " + adminEmail);
	        }
	    };
	}*/
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

	            // 🔗 Chemin de l’image
	            String sourceImagePath = "uploads/1f93d292-c538-4bbe-867b-939e928b4c15_PHOTO.jpg";
	            String fileName = UUID.randomUUID().toString() + "_" + new File(sourceImagePath).getName();
	            Path destinationPath = Paths.get("uploads").resolve(fileName);
	            Files.createDirectories(destinationPath.getParent());
	            Files.copy(Paths.get(sourceImagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);

	            String imageUrl = "http://localhost:8082/uploads/" + fileName;
	            Image image = new Image(imageUrl);

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
	    };
	}

}
