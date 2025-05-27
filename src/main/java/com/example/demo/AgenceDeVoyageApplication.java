package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

@SpringBootApplication
public class AgenceDeVoyageApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenceDeVoyageApplication.class, args);
	}
	@Bean
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
	}
	

}
