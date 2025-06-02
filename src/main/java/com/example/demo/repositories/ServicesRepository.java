package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Services;

public interface ServicesRepository extends JpaRepository<Services, Long> {
    Optional<Services> findByName(String name);
}
