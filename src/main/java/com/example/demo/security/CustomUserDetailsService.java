package com.example.demo.security;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructeur explicite
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // On récupère directement l'objet User (qui implémente UserDetails)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Optionnel : afficher le mot de passe (utile pour debug, à retirer en prod)
        System.out.println("User password from DB: " + user.getPassword());

        // On retourne l'entité User qui est un UserDetails complet
        return user;
    }
}
