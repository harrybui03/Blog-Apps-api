package com.example.demo.uploadFile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileUpload,Long> {
}
