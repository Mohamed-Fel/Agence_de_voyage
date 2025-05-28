package com.example.demo.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.demo.entities.Agent;
import com.example.demo.entities.User;
import com.example.demo.repositories.AgentRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AgentService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class AgentServiceImpl implements AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findByRoleId(2L); // Role "Agent"
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec ID: " + id));
    }

    @Override
    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agent non trouvé avec ID: " + id));
    }

    @Override
    public Agent updateAgent(Long id, Agent updatedAgent) {
        Agent existing = getAgentById(id);
        existing.setFirstName(updatedAgent.getFirstName());
        existing.setLastName(updatedAgent.getLastName());
        existing.setEmail(updatedAgent.getEmail());
        existing.setUserName(updatedAgent.getUserName());
        if (updatedAgent.getImage() != null) {
            existing.setImage(updatedAgent.getImage());
        }
        return agentRepository.save(existing);
    }

    @Override
    public void deleteAgent(Long id) {
        Agent agent = getAgentById(id);
        agentRepository.delete(agent);
    }
}


