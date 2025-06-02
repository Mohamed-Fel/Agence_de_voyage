package com.example.demo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Contrat;

public interface ContratService {
	
	 Contrat addContrat(String nomProduit, String startDate, String endDate, MultipartFile file) throws Exception;
	 Contrat updateContrat(Long id, String nomProduit, String startDate, String endDate, MultipartFile file) throws Exception;
     void deleteContrat(Long id);
	 List<Contrat> getAllContrats();
	 Contrat getContratByNomProduit(String nomProduit);
	 Contrat getContratById(Long id);
	 List<Contrat> getContratsNonAssignes();
	 
}
