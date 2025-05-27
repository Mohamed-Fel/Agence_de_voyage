package com.example.demo.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Agent")
public class Agent extends User {
	private static final long serialVersionUID = 1L;

	public Agent() {
		// TODO Auto-generated constructor stub
	}

	public Agent(String userName, String firstName, String lastName, String email, String password, Image image,
			Role role) {
		super(userName, firstName, lastName, email, password, image, role);
		// TODO Auto-generated constructor stub
	}
}
