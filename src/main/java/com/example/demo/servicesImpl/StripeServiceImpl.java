package com.example.demo.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Reservation;
import com.example.demo.entities.StripeResponse;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import com.stripe.param.checkout.SessionCreateParams;
@Service
public class StripeServiceImpl implements StripeService {
	   @Value("${stripe.secretKey}")
	    private String secretKey;

	    @Value("${stripe.success.url}")
	    private String successUrl;

	    @Value("${stripe.cancel.url}")
	    private String cancelUrl;
        @Autowired
	    private ReservationRepository reservationRepository;

	    @Override
	    public StripeResponse createStripeSession(Long reservationId) throws StripeException {
	        Stripe.apiKey = secretKey;

	        Reservation reservation = reservationRepository.findById(reservationId)
	                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

	        long montantEnCentimes = (long) (reservation.getTotalPrixReservation() * 100);

	        SessionCreateParams params = SessionCreateParams.builder()
	                .setMode(SessionCreateParams.Mode.PAYMENT)
	                .setSuccessUrl(successUrl)
	                .setCancelUrl(cancelUrl)
	                .addLineItem(
	                        SessionCreateParams.LineItem.builder()
	                                .setPriceData(
	                                        SessionCreateParams.LineItem.PriceData.builder()
	                                                .setCurrency("usd")
	                                                .setUnitAmount(montantEnCentimes)
	                                                .setProductData(
	                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
	                                                                .setName("Paiement réservation #" + reservationId)
	                                                                .build()
	                                                )
	                                                .build()
	                                )
	                                .setQuantity(1L)
	                                .build()
	                )
	                .putMetadata("reservationId", reservationId.toString())
	                .setPaymentIntentData(
	                	    SessionCreateParams.PaymentIntentData.builder()
	                	        .putMetadata("reservationId", reservationId.toString())
	                	        .build()
	                	)
	                .build();

	        Session session = Session.create(params);

	        return StripeResponse.builder()
	                .status("SUCCESS")
	                .message("Session Stripe créée avec succès")
	                .sessionId(session.getId())
	                .sessionUrl(session.getUrl())
	                .build();
	    }
	}

