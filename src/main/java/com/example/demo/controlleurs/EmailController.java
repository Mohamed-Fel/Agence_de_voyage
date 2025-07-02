package com.example.demo.controlleurs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.EmailService;

import jakarta.mail.MessagingException;
@RestController
@RequestMapping("/api/email")
public class EmailController {
	 @Autowired
	 private EmailService emailService;

	 @PostMapping("/send-pdf")
	 public ResponseEntity<?> sendEmailWithPdf(
	         @RequestParam String email,
	         @RequestParam("file") MultipartFile file) {
	        try {
	            emailService.sendReservationWithPdf(email, file);
	            return ResponseEntity.ok().body("✅ Email envoyé avec succès à " + email);
	        } catch (MessagingException | IOException e) {
	            return ResponseEntity.internalServerError()
	                    .body("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
	        }
	    }

}
