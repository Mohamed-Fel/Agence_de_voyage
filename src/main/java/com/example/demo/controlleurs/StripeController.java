package com.example.demo.controlleurs;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Paiement;
import com.example.demo.entities.Reservation;
import com.example.demo.entities.StripeResponse;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.repositories.PaiementRepository;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;

/*@RestController
@RequestMapping("/stripe")
public class StripeController {
	   @Autowired
	   private  ReservationRepository reservationRepository;
	   @Autowired
	   private  PaiementRepository paiementRepository;
	   @Autowired
	   private StripeService stripeService;
	   
	    @Value("${stripe.secretKey}")
	    private String secretKey;

	    @Value("${stripe.webhook.secret}")
	    private String endpointSecret;
	    
	    @PostMapping("/checkout/{reservationId}")
	    public ResponseEntity<StripeResponse> checkoutReservation(@PathVariable Long reservationId) throws StripeException {
	        StripeResponse stripeResponse = stripeService.createStripeSession(reservationId);
	        return ResponseEntity.ok(stripeResponse);
	    }    
	@PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
	                                                @RequestHeader("Stripe-Signature") String sigHeader) {
	    System.out.println("Payload brut: " + payload);
	    System.out.println("Signature: " + sigHeader);
	    Stripe.apiKey = secretKey;

	    Event event;
	    try {
	        event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
	    } catch (SignatureVerificationException e) {
	    	System.out.println("⚠️ Signature invalide : " + e.getMessage());
	        return ResponseEntity.badRequest().body("Invalid signature");
	    }

	    String eventType = event.getType();

	    switch(eventType) {
	        case "checkout.session.completed":
	        case "payment_intent.succeeded":
	            handlePaymentSucceeded(event);
	            break;

	        case "payment_intent.payment_failed":
	            handlePaymentFailed(event);
	            break;

	        case "charge.succeeded":
	            handleChargeSucceeded(event);
	            break;

	        default:
	            // Event non géré
	            break;
	    }

	    return ResponseEntity.ok("Webhook reçu");
	}

	private void handlePaymentSucceeded(Event event) {
	    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
	    if (paymentIntent == null) return;

	    String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
	    if (reservationIdStr == null) return;

	    Long reservationId = Long.parseLong(reservationIdStr);
	    Reservation reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

	    reservation.setStatus(ReservationStatus.CONFIRMED);
	    reservationRepository.save(reservation);

	    Paiement paiement = paiementRepository.findByReservation(reservation)
	            .orElseGet(() -> new Paiement());

	    paiement.setMontantPaye(reservation.getTotalPrixReservation());
	    paiement.setPaiementStatus("PAID");
	    paiement.setPaymentType("Carte bancaire"); // Par défaut, sera affiné par charge.succeeded
	    paiement.setDatePaiement(LocalDateTime.now());
	    paiement.setReservation(reservation);

	    paiementRepository.save(paiement);
	}

	private void handlePaymentFailed(Event event) {
	    PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
	    if (paymentIntent == null) return;

	    String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
	    if (reservationIdStr == null) return;

	    Long reservationId = Long.parseLong(reservationIdStr);
	    Reservation reservation = reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new RuntimeException("Réservation introuvable"));

	    reservation.setStatus(ReservationStatus.CANCELLED);
	    reservationRepository.save(reservation);

	    Paiement paiement = paiementRepository.findByReservation(reservation)
	            .orElseGet(() -> new Paiement());

	    paiement.setMontantPaye(0.0);
	    paiement.setPaiementStatus("FAILED");
	    paiement.setPaymentType("Carte bancaire");
	    paiement.setDatePaiement(LocalDateTime.now());
	    paiement.setReservation(reservation);

	    paiementRepository.save(paiement);
	}

	private void handleChargeSucceeded(Event event) {
	    Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElse(null);
	    if (charge == null) return;

	    String paymentIntentId = charge.getPaymentIntent();

	    PaymentIntent paymentIntent = null;
	    try {
	        paymentIntent = PaymentIntent.retrieve(paymentIntentId);
	    } catch (Exception e) {
	        // handle error
	        return;
	    }

	    String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
	    if (reservationIdStr == null) return;

	    Long reservationId = Long.parseLong(reservationIdStr);
	    Paiement paiement = paiementRepository.findByReservationId(reservationId)
	            .orElse(null);

	    if (paiement != null) {
	        String cardBrand = charge.getPaymentMethodDetails().getCard().getBrand();
	        paiement.setPaymentType(cardBrand); // ex: visa, mastercard...
	        paiementRepository.save(paiement);
	    }
	}
}*/
@RestController
@RequestMapping("/stripe")
public class StripeController {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private PaiementRepository paiementRepository;
    
    @Autowired
    private StripeService stripeService;
    
    @Value("${stripe.secretKey}")
    private String secretKey;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    
    @PostMapping("/checkout/{reservationId}")
    public ResponseEntity<StripeResponse> checkoutReservation(@PathVariable Long reservationId) throws StripeException {
        StripeResponse stripeResponse = stripeService.createStripeSession(reservationId);
        return ResponseEntity.ok(stripeResponse);
    }
    
    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        System.out.println("Received webhook payload: " + payload); // Ajoutez ce log
        System.out.println("Received signature: " + sigHeader); // Ajoutez ce log
        
        Stripe.apiKey = secretKey;

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Processing event type: " + event.getType());
        } catch (SignatureVerificationException e) {
            System.err.println("⚠️ Signature verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        try {
            switch(event.getType()) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    break;
                /*case "payment_intent.succeeded":
                    //handlePaymentSucceeded(event);
                	System.out.println("Payment intent succeeded event ignored - using checkout.session.completed");
                    break;*/
                case "payment_intent.payment_failed":
                    handlePaymentFailed(event);
                    break;
                case "charge.succeeded":
                case "charge.updated":
                    handleChargeEvent(event);
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
            }
        } catch (Exception e) {
            System.err.println("Error processing event: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error processing webhook");
        }

        return ResponseEntity.ok("Webhook processed successfully");
    }

    private void handleCheckoutSessionCompleted(Event event) throws StripeException {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow(
            () -> new RuntimeException("Failed to deserialize session"));
        
        String reservationIdStr = session.getMetadata().get("reservationId");
        if (reservationIdStr == null) {
            throw new RuntimeException("reservationId not found in session metadata");
        }

        Long reservationId = Long.parseLong(reservationIdStr);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + reservationId));

        Paiement paiement = paiementRepository.findByReservation(reservation)
                .orElse(new Paiement());

        // Utilisation de la nouvelle méthode getPaymentMethodType
        String paymentType = getPaymentMethodType(session);
        
        paiement.setMontantPaye(session.getAmountTotal() / 100.0);
        paiement.setPaiementStatus("PAID");
        paiement.setPaymentType(paymentType);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setReservation(reservation);

        paiementRepository.save(paiement);

        reservation.setPaiement(paiement); 
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        
        System.out.println("Payment recorded - Type: " + paymentType + ", Amount: " + paiement.getMontantPaye());
    }

    private void handlePaymentSucceeded(Event event) throws StripeException {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow(
            () -> new RuntimeException("PaymentIntent is null"));

        String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
        if (reservationIdStr == null) {
            throw new RuntimeException("reservationId not found in payment intent metadata");
        }

        Long reservationId = Long.parseLong(reservationIdStr);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found: " + reservationId));

        Paiement paiement = paiementRepository.findByReservation(reservation)
                .orElse(new Paiement());

        // Nouvelle méthode pour récupérer le type de paiement
        String paymentType = "Méthode inconnue";
        if (paymentIntent.getPaymentMethod() != null) {
            PaymentMethod method = PaymentMethod.retrieve(paymentIntent.getPaymentMethod());
            paymentType = getDetailedPaymentType(method);
        }
        
        paiement.setMontantPaye(paymentIntent.getAmount() / 100.0);
        paiement.setPaiementStatus("PAID");
        paiement.setPaymentType(paymentType);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setReservation(reservation);

        paiementRepository.save(paiement);
        reservation.setPaiement(paiement);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }

    private void handlePaymentFailed(Event event) throws StripeException {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow(
            () -> new RuntimeException("PaymentIntent is null"));

        String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
        if (reservationIdStr == null) return;

        Long reservationId = Long.parseLong(reservationIdStr);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Paiement paiement = paiementRepository.findByReservation(reservation)
                .orElse(new Paiement());

        // Nouvelle méthode pour récupérer le type de paiement
        String paymentType = "Méthode inconnue";
        if (paymentIntent.getPaymentMethod() != null) {
            PaymentMethod method = PaymentMethod.retrieve(paymentIntent.getPaymentMethod());
            paymentType = getDetailedPaymentType(method);
        }

        paiement.setMontantPaye(0.0);
        paiement.setPaiementStatus("FAILED");
        paiement.setPaymentType(paymentType);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setReservation(reservation);

        paiementRepository.save(paiement);
        reservation.setPaiement(paiement);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        
        System.out.println("❌ Paiement échoué - Méthode: " + paymentType);
    }

    private void handleChargeEvent(Event event) throws StripeException {
        Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElseThrow(
            () -> new RuntimeException("Charge is null"));

        PaymentIntent paymentIntent = PaymentIntent.retrieve(charge.getPaymentIntent());
        String reservationIdStr = paymentIntent.getMetadata().get("reservationId");
        if (reservationIdStr == null) return;

        Long reservationId = Long.parseLong(reservationIdStr);
        Paiement paiement = paiementRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("Paiement not found for reservation: " + reservationId));

        // Mise à jour des détails de la carte si disponible
        if (charge.getPaymentMethodDetails() != null && charge.getPaymentMethodDetails().getCard() != null) {
            String cardBrand = charge.getPaymentMethodDetails().getCard().getBrand();
            paiement.setPaymentType(translateCardBrand(cardBrand));
            paiementRepository.save(paiement);
        }
    }

    // Vos nouvelles méthodes (inchangées)
    private String getPaymentMethodType(Session session) throws StripeException {
        // 1. Si c'est un paiement simple
        if (session.getPaymentIntent() != null) {
            PaymentIntent intent = PaymentIntent.retrieve(session.getPaymentIntent());
            if (intent.getPaymentMethod() != null) {
                PaymentMethod method = PaymentMethod.retrieve(intent.getPaymentMethod());
                return getDetailedPaymentType(method);
            }
        }
        
        // 2. Si c'est un abonnement
        if (session.getSetupIntent() != null) {
            SetupIntent setup = SetupIntent.retrieve(session.getSetupIntent());
            if (setup.getPaymentMethod() != null) {
                PaymentMethod method = PaymentMethod.retrieve(setup.getPaymentMethod());
                return getDetailedPaymentType(method);
            }
        }
        
        // 3. Fallback
        if (!session.getPaymentMethodTypes().isEmpty()) {
            return translatePaymentType(session.getPaymentMethodTypes().get(0));
        }
        
        return "Méthode inconnue";
    }

    private String getDetailedPaymentType(PaymentMethod method) {
        if ("card".equals(method.getType())) {
            PaymentMethod.Card card = method.getCard();
            String brand = card.getBrand();
            return translateCardBrand(brand);
        }
        return translatePaymentType(method.getType());
    }

    private String translateCardBrand(String brand) {
        if (brand == null) return "Carte";
        
        switch (brand.toLowerCase()) {
            case "visa": return "Visa";
            case "mastercard": return "Mastercard";
            case "amex": return "American Express";
            case "discover": return "Discover";
            case "jcb": return "JCB";
            case "diners": return "Diners Club";
            case "unionpay": return "UnionPay";
            default: return brand;
        }
    }

    private String translatePaymentType(String stripeType) {
        switch (stripeType.toLowerCase()) {
            case "card": return "Carte bancaire";
            case "paypal": return "PayPal";
            case "sepa_debit": return "Prélèvement SEPA";
            case "sofort": return "Sofort";
            case "ideal": return "iDEAL";
            case "bancontact": return "Bancontact";
            case "alipay": return "Alipay";
            default: return stripeType;
        }
    }
}
