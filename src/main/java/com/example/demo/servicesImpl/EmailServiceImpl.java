package com.example.demo.servicesImpl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.EmailService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Service
public class EmailServiceImpl implements EmailService {
	
	 	@Autowired
	 	private JavaMailSender mailSender;

	    @Override
	    public void sendReservationWithPdf(String toEmail, MultipartFile pdfFile) throws MessagingException, IOException {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        helper.setTo(toEmail);
	        helper.setSubject("Votre réservation");
	        helper.setText("Bonjour,\n\nVeuillez trouver ci-joint votre réservation au format PDF.");

	        InputStreamSource attachment = new ByteArrayResource(pdfFile.getBytes());

	        helper.addAttachment("reservation.pdf", attachment);

	        mailSender.send(message);
	    }
	    /*@Override
	    public void sendResetPasswordEmail(String to, String resetUrl) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("Réinitialisation de mot de passe");
	        message.setText("Cliquez sur ce lien pour réinitialiser votre mot de passe : " + resetUrl);
	        mailSender.send(message);
	    }*/
	    @Override
	    public void sendResetCodeEmail(String to, String code) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("🔐 Code de réinitialisation de mot de passe");
	        message.setText("Bonjour,\n\nVoici votre code de réinitialisation : " + code + 
	                        "\n\nCe code est valable pendant 1 heure.\n\nL'équipe support.");
	        mailSender.send(message);
	    }

}
