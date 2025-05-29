package com.example.demo.entities;

import java.util.List;
import java.util.Set;

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

	    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{7,15}$", message = "Numéro de téléphone invalide")
	    private String phoneNumber;

	    @Email(message = "Email invalide")
	    private String email;

	    @Min(value = 0, message = "Le nombre d'étoiles doit être >= 0")
	    @Max(value = 5, message = "Le nombre d'étoiles ne peut pas dépasser 5")
	    private int nbEtoiles;

	    private String pays;

	    private String country;

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
	        name = "produit_service",
	        joinColumns = @JoinColumn(name = "produit_id"),
	        inverseJoinColumns = @JoinColumn(name = "service_id")
	    )
	    private Set<Service> services;
	    
	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "localisation_id")
	    private Localisation localisation;
	    
	    @OneToOne(cascade = CascadeType.ALL)
	    @JoinColumn(name = "contrat_id")
	    private Contrat contrat;
	    
}
