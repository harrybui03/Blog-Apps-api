package com.example.demo.uploadFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private String fileName;
    private String fileType;
    private String uploadDir;
}
