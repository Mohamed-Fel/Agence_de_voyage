package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {

}
