package com.example.demo.servicesImpl;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Fichier vide");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);
        Files.createDirectories(uploadPath); // s'assure que le dossier existe

        Path filePath = uploadPath.resolve(fileName); // uploads/<uuid>_filename.ext
        Files.write(filePath, file.getBytes());
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        String imageUrl = "http://localhost:8082/uploads/" + fileName;

        // Retourner l’URL ou chemin (ici juste le nom)
        return imageUrl;
    }
}
