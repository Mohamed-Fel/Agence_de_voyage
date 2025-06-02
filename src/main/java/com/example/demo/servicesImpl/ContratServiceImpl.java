package com.example.demo.servicesImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Contrat;
import com.example.demo.repositories.ContratRepository;
import com.example.demo.services.ContratService;
import com.example.demo.services.FileStorageService;

@Service
public class ContratServiceImpl implements ContratService {

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Contrat addContrat(String nomHotel, String startDate, String endDate, MultipartFile file) throws Exception {
        Contrat contrat = new Contrat();
        contrat.setNomProduit(nomHotel);
        contrat.setStartDate(LocalDate.parse(startDate));
        contrat.setEndDate(LocalDate.parse(endDate));
        contrat.setFileUrl(fileStorageService.saveImage(file));
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat updateContrat(Long id, String nomHotel, String startDate, String endDate, MultipartFile file) throws Exception {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contrat introuvable"));
        contrat.setNomProduit(nomHotel);
        contrat.setStartDate(LocalDate.parse(startDate));
        contrat.setEndDate(LocalDate.parse(endDate));

        if (file != null && !file.isEmpty()) {
            contrat.setFileUrl(fileStorageService.saveImage(file));
        }

        return contratRepository.save(contrat);
    }

    @Override
    public void deleteContrat(Long id) {
        Contrat contrat = contratRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Contrat introuvable avec l'ID : " + id));
        contratRepository.delete(contrat);
    }
    @Override
    public List<Contrat> getContratsNonAssignes() {
        return contratRepository.findContratsSansProduit();
    }

    @Override
    public List<Contrat> getAllContrats() {
        return contratRepository.findAll();
    }
    @Override
    public Contrat getContratById(Long id) {
        return contratRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Contrat introuvable avec l'ID : " + id));
    }

    @Override
    public Contrat getContratByNomProduit(String nomProduit) {
        return contratRepository.findByNomProduit(nomProduit)
                .orElseThrow(() -> new NoSuchElementException("Contrat introuvable pour cet hôtel"));
    }
}
