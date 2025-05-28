package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Agent;
import com.example.demo.entities.User;

public interface AgentService {
	
    List<Agent> getAllAgents();
    User getUserById(Long id);
    Agent getAgentById(Long id);
    Agent updateAgent(Long id, Agent updatedAgent);
    void deleteAgent(Long id);

}
