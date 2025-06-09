package com.example.demo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "room")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "room")
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @NotNull(message = "Le prix est obligatoire.")
    @Positive(message = "Le prix doit être un nombre positif.")
    private Double  prix;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "room_id", nullable = true)
    @JsonBackReference
    private Room room;
   
    
    @JsonProperty("roomId")
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }
}