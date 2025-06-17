package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@ToString(exclude = {"produit", "reservations"})
@EqualsAndHashCode(exclude = {"produit" ,"reservations"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacite;
    @Lob
    private String description;
    private int nbDeLit;
    private double prixAdulte;
    private double prixEnfant;
    private int ageMinimal;
    /*@JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Logement> logements;*/
    /*@JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;*/
    
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Logement> logements;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
    
    /*@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id") // la FK sera dans la table "image"
    private List<Image> images = new ArrayList<>();*/
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
    
    @ManyToMany(mappedBy = "rooms")
    @JsonBackReference
    private List<Reservation> reservations;
    
    public Long getProduitId() {
        return produit != null ? produit.getId() : null;
    }
    
}
