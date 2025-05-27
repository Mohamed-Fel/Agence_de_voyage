package com.example.demo.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")

public class Admin extends User  {
	private static final long serialVersionUID = 1L;

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String userName, String firstName, String lastName, String email, String password, Image image,
			Role role) {
		super(userName, firstName, lastName, email, password, image, role);
		// TODO Auto-generated constructor stub
	}

}
