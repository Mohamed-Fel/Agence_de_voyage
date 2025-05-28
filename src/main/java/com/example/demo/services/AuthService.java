package com.example.demo.services;

import java.util.Map;

public interface AuthService {
	Map<String, Object> login(String email, String password);
}