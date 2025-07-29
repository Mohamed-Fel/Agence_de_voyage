package com.example.demo.services;

import com.example.demo.entities.StripeResponse;
import com.stripe.exception.StripeException;


public interface StripeService {
	StripeResponse createStripeSession(Long reservationId) throws StripeException;
}
