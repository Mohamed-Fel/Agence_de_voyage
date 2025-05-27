package com.example.demo.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;



public interface FileStorageService {
	String saveImage(MultipartFile file) throws IOException;

}
