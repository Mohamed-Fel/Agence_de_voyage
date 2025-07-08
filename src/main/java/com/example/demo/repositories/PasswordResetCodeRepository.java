package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.PasswordResetCode;

import jakarta.transaction.Transactional;
@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByEmailAndCode(String email, String code);
    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetCode p WHERE p.email = ?1")
    void deleteByEmail(String email); 

}
