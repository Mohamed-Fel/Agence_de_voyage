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
	        MimeMessage message = mailSender.createMimeMessage();

	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	            helper.setTo(to);
	            helper.setSubject("🔐 Code de réinitialisation de mot de passe");

	            String htmlContent = """
	                <html>
	                <body style="font-family: Arial, sans-serif; color: #333;">
	                    <h2>Réinitialisation de mot de passe</h2>
	                    <p>Bonjour,</p>
	                    <p>Voici votre code de réinitialisation :</p>
	                    
	                    <div style="padding: 15px; border: 2px dashed #007bff; background-color: #f0f8ff; 
	                                font-size: 24px; font-weight: bold; text-align: center; width: fit-content;">
	                        %s
	                    </div>

	                    <p style="margin-top: 20px;">⏳ Ce code est valable pendant <strong>3 minutes</strong>.</p>
	                    <p>Si vous n'avez pas demandé de réinitialisation, ignorez ce message.</p>

	                    <p style="margin-top: 30px;">Cordialement,<br>L’équipe support</p>
	                </body>
	                </html>
	                """.formatted(code);

	            helper.setText(htmlContent, true); // true = HTML content
	            mailSender.send(message);

	        } catch (MessagingException e) {
	            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail HTML", e);
	        }
	    }

}
