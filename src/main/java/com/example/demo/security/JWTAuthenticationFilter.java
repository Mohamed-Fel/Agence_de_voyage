package com.example.demo.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/auth/login"); // URL pour login
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Lire le corps JSON de la requête
            JsonNode jsonNode = new ObjectMapper().readTree(request.getInputStream());

            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();

            // Crée un token avec email et password pour Spring Security
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // Générer token JWT avec info utilisateur (principal)
        String token = jwtService.generateToken(authResult);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Authentication failed: " + failed.getMessage() + "\"}");
        response.getWriter().flush();
    }
}*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setFilterProcessesUrl("/auth/login");
    }

    /*@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(request.getInputStream());

            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();

            logger.info("Tentative d'authentification avec email: {}", email);
            if (password == null || password.isEmpty()) {
                logger.warn("Mot de passe vide reçu !");
            } else {
                logger.info("Mot de passe reçu (masqué) : {}", "*".repeat(password.length()));
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du corps de la requête", e);
            throw new RuntimeException("Failed to parse authentication request body");
        }
    }*/
    /*@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // Lire les paramètres directement depuis la requête (form-data ou query param)
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("Tentative d'authentification avec email: " + email);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // Ici Spring va charger le UserDetails et comparer les mots de passe avec BCrypt
        return authenticationManager.authenticate(authToken);
    }*/

    /*@Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtService.generateToken(authResult);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }*/
    /*@Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtService.generateToken(authResult);
        

        // Récupérer l'utilisateur authentifié
        Object principal = authResult.getPrincipal();
        
        // Si tu utilises ton entité User directement comme UserDetails
        com.example.demo.entities.User user = (com.example.demo.entities.User) principal;

        // Masquer le mot de passe
        user.setPassword(null);

        // Créer un objet JSON contenant le token et l'utilisateur
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", user);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.warn("Échec de l'authentification : {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Authentication failed: " + failed.getMessage() + "\"}");
        response.getWriter().flush();
    }*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(request.getInputStream());

            // Vérification présence et validité des champs
            if (jsonNode.get("email") == null || jsonNode.get("email").asText().isEmpty()) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Le champ 'email' est obligatoire.");
                return null; // Arrête la tentative d'authentification
            }
            if (jsonNode.get("password") == null || jsonNode.get("password").asText().isEmpty()) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Le champ 'password' est obligatoire.");
                return null; // Arrête la tentative d'authentification
            }

            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();

            logger.info("Tentative d'authentification avec email: {}", email);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du corps de la requête", e);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Requête mal formée : JSON invalide.");
            return null;
        }
    }

    // Méthode utilitaire pour envoyer un message d'erreur JSON
    private void sendErrorResponse(HttpServletResponse response, int status, String message) {
        try {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = String.format("{\"message\": \"%s\"}", message);
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
            logger.error("Erreur lors de l'envoi de la réponse d'erreur", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtService.generateToken(authResult);

        Object principal = authResult.getPrincipal();
        com.example.demo.entities.User user = (com.example.demo.entities.User) principal;

        user.setPassword(null); // Masquer le mot de passe

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", user);
        responseBody.put("message", "Authentification réussie.");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.warn("Échec de l'authentification : {}", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Message clair avec champ message
        String json = String.format("{\"message\": \"Échec de l'authentification : %s\"}", failed.getMessage());
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}

