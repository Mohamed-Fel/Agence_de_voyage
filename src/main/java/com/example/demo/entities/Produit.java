package com.example.demo.entities;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class Produit {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank(message = "Le nom est obligatoire")
	    private String name;

	    private String description;

	    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{8,14}$", message = "Le numéro de téléphone est invalide")
	    private String phoneNumber;

	    @Email(message = "Email invalide")
	    private String email;

	    @Min(value = 0, message = "Le nombre d'étoiles doit être >= 0")
	    @Max(value = 5, message = "Le nombre d'étoiles ne peut pas dépasser 5")
	    private int nbEtoiles;

	    private String ville;

	    private String pays;

	    private String adresse;
	    
	    private int initialPrix;
	    
	    
	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User creator;
	    
	    @ManyToOne
	    @JoinColumn(name = "categorie_id")
	    private Categorie categorie;
	    
	    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Image> images;
	    
	    @ManyToMany
	    @JoinTable(
	        name = "produit_services",
	        joinColumns = @JoinColumn(name = "produit_id"),
	        inverseJoinColumns = @JoinColumn(name = "service_id")
	    )
	    private List<Services> services;
	    
	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "localisation_id")
	    private Localisation localisation;
	    
	    @OneToOne
	    @JoinColumn(name = "contrat_id")
	    private Contrat contrat;
	    
	    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Room> rooms ;
}
