package com.example.demo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Contrat;

public interface ContratService {
	
	 Contrat addContrat(String nomHotel, String startDate, String endDate, MultipartFile file) throws Exception;
	 Contrat updateContrat(Long id, String nomHotel, String startDate, String endDate, MultipartFile file) throws Exception;
     void deleteContrat(Long id);
	 List<Contrat> getAllContrats();
	 Contrat getContratByNomHotel(String nomHotel);
	 Contrat getContratById(Long id);
	 
}
