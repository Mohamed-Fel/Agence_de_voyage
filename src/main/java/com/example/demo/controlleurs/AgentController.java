package com.example.demo.controlleurs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import com.example.demo.entities.Agent;
import com.example.demo.repositories.AgentRepository;
import com.example.demo.services.AgentService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/manage")
public class AgentController {
	
	    @Autowired
	    private AgentService agentService;
	    @Autowired
	    private AgentRepository agentRepository;

	    @GetMapping("/agents")
	    public List<Agent> getAllAgents() {
	        return agentService.getAllAgents();
	    }

	    /*@GetMapping("/agents/{id}")
	    public Agent getAgentById(@PathVariable Long id) {
	        return agentService.getAgentById(id);
	    }*/
	    @GetMapping("/agents/{id}")
	    public ResponseEntity<?> getAgentById(@PathVariable Long id) {
	        Optional<Agent> optionalAgent = agentRepository.findById(id);

	        if (optionalAgent.isPresent()) {
	        	Map<String, Object> response = new HashMap<>();
	        	response.put("message", "Agent avec l'ID " + id + " est existe bien");
	            return ResponseEntity.ok(optionalAgent.get());
	        } else {
	            Map<String, Object> response = new HashMap<>();
	            response.put("message", "Agent avec l'ID " + id + " n'existe pas.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	    }

	    @PutMapping("/agents/{id}")
	    public Agent updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
	        return agentService.updateAgent(id, agent);
	    }

	    @DeleteMapping("/agents/{id}")
	    public void deleteAgent(@PathVariable Long id) {
	        agentService.deleteAgent(id);
	    }

}
