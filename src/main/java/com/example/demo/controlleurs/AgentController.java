package com.example.demo.controlleurs;

import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import com.example.demo.entities.Agent;
import com.example.demo.entities.Image;
import com.example.demo.repositories.AgentRepository;
import com.example.demo.services.AgentService;
import com.example.demo.services.FileStorageService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/manage")
public class AgentController {
	
	    @Autowired
	    private AgentService agentService;
	    @Autowired
	    private FileStorageService fileStorageService;
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
	            response.put("message", "Agent avec l'ID " + id + " existe bien.");
	            response.put("agent", optionalAgent.get()); // ✅ inclure les données de l'agent
	            return ResponseEntity.ok(response);         // ✅ retourne la map complète
	        } else {
	            Map<String, Object> response = new HashMap<>();
	            response.put("message", "Agent avec l'ID " + id + " n'existe pas.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }
	    }
	    @PutMapping(value = "/agents/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<?> updateAgent(
	            @PathVariable Long id,
	            @RequestParam("userName") String userName,
	            @RequestParam("firstName") String firstName,
	            @RequestParam("lastName") String lastName,
	            @RequestParam("email") String email,
	            @RequestPart(value = "image", required = false) MultipartFile imageFile
	    ) {
	        try {
	            // 🔄 Récupérer l'agent existant
	            Agent existingAgent = agentService.getAgentById(id);

	            // 📝 Mettre à jour les champs
	            existingAgent.setUserName(userName);
	            existingAgent.setFirstName(firstName);
	            existingAgent.setLastName(lastName);
	            existingAgent.setEmail(email);

	            // 📷 Si une image est envoyée, la sauvegarder et mettre à jour
	            if (imageFile != null && !imageFile.isEmpty()) {
	                String imageUrl = fileStorageService.saveImage(imageFile);
	                agentService.updateAgentImage(existingAgent, imageUrl);
	            }

	            // 💾 Sauvegarder les modifications
	            Agent updated = agentService.updateAgent(id, existingAgent);
	            updated.setPassword(null); // Ne pas retourner le mot de passe

	            // ✅ Réponse
	            Map<String, Object> response = new HashMap<>();
	            response.put("message", "✅ Agent mis à jour avec succès.");
	            response.put("agent", updated);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {
	            Map<String, String> error = new HashMap<>();
	            error.put("error", "❌ Erreur lors de la mise à jour : " + e.getMessage());
	            return ResponseEntity.badRequest().body(error);
	        }
	    }

	    /*@DeleteMapping("/agents/{id}")
	    public void deleteAgent(@PathVariable Long id) {
	        agentService.deleteAgent(id);
	    }*/
	    @DeleteMapping("/agents/{id}")
	    public ResponseEntity<Map<String, Object>> deleteAgent(@PathVariable Long id) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            agentService.deleteAgent(id);
	            response.put("success", true);
	            response.put("message", "L'agent a été supprimé avec succès.");
	            return ResponseEntity.ok(response);
	        } catch (NoSuchElementException e) {
	            response.put("success", false);
	            response.put("message", "Agent introuvable avec l'ID : " + id);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        } catch (Exception e) {
	            response.put("success", false);
	            response.put("message", "Une erreur est survenue lors de la suppression de l'agent.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }

}
