package com.example.demo.servicesImpl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.services.AuthService;
import com.example.demo.entities.User;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService ,UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository= userRepository;
    }


    @Autowired
    private final UserRepository userRepository;

    public Map<String, Object> login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            String token = jwtService.generateToken(authentication);
            System.out.println("User to return: " + token);
            

            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("User to return: " + user);
            

            user.setPassword(null); // Masquer le mot de passe dans la réponse

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);

            return response;
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password");
        }
    }
    /*@Override
    public String login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            return jwtService.generateToken(authentication);
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password");
        }
    }*/

}
