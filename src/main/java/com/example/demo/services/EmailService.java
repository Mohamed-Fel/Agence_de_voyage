package com.example.demo.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import jakarta.mail.MessagingException;

public interface EmailService {
	void sendReservationWithPdf(String toEmail, MultipartFile pdfFile) throws MessagingException, IOException;
	void sendResetCodeEmail(String to, String code);

}
