package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Admin;
import com.example.demo.entities.Agent;
import com.example.demo.entities.User;

public interface UserService {
	Agent createAgent(Agent agent )throws Exception;
	Admin editAdminProfile(Long adminId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception;
	User editUserProfile(Long id, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception;
	
	Agent editAgentProfile(Long agentId, String userName, String firstName, String lastName, String email, String password, MultipartFile imageFile) throws Exception;
}
