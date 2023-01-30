package com.example.demo.uploadFile;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    public String storeFile(MultipartFile file, Long id);

    public FileDTO uploadFile(MultipartFile file , Long id);

    public List<FileDTO> uploadMultipleFiles(MultipartFile[] files, Long id);

}
