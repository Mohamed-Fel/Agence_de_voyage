package com.example.demo.controlleurs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repositories.PasswordResetCodeRepository;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.EmailService;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import com.example.demo.entities.PasswordResetCode;
import com.example.demo.entities.User;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/reset")
public class ForgotPasswordController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetCodeRepository codeRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun utilisateur avec cet email.");
        }

        // Supprimer anciens tokens
        codeRepository.deleteByEmail(email);

     // Générer un code OTP à 6 chiffres
        String code = String.format("%06d", new Random().nextInt(999999));
        Date expiryDate = new Date(System.currentTimeMillis() + 180_000);; // 3 minutes

        PasswordResetCode resetCode = new PasswordResetCode(code, email, expiryDate);
        codeRepository.save(resetCode);

        emailService.sendResetCodeEmail(email, code);
        return ResponseEntity.ok("✅ Code envoyé par email");
    }
    // Étape 2 : Vérifier le code
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
        Optional<PasswordResetCode> optionalCode = codeRepository.findByEmailAndCode(email, code);

        if (optionalCode.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ Code incorrect");
        }

        if (optionalCode.get().isExpired()) {
            return ResponseEntity.badRequest().body("⏰ Code invalide");
        }

        return ResponseEntity.ok("✅ Code valide");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
                                           @RequestParam String code,
                                           @RequestParam String newPassword) {
        Optional<PasswordResetCode> optionalCode = codeRepository.findByEmailAndCode(email, code);

        if (optionalCode.isEmpty() || optionalCode.get().isExpired()) {
            return ResponseEntity.badRequest().body("❌ Code invalide ou expiré");
        }

        User user = userRepository.findByEmail(email).get();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        codeRepository.deleteByEmail(email);

        return ResponseEntity.ok("🔒 Mot de passe réinitialisé avec succès");
    }
}
