package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	
    List<Agent> findByRoleId(Long roleId);
    Optional<Agent> findById(Long id);

}
