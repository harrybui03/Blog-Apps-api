package com.example.demo.uploadFile.impl;

import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.posts.Post;
import com.example.demo.posts.PostDto;
import com.example.demo.posts.PostRepository;
import com.example.demo.uploadFile.FileRepository;
import com.example.demo.uploadFile.FileService;
import com.example.demo.uploadFile.FileDTO;
import com.example.demo.uploadFile.FileUpload;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServicePost implements FileService {

    private final Path fileStorageLocation;

    private final FileRepository fileRepository;

    private final PostRepository postRepository;

    private final ModelMapper mapper;

    public FileServicePost(FileRepository fileRepository, PostRepository postRepository , ModelMapper mapper , FileUpload fileUpload) {
        this.fileStorageLocation = Paths.get(fileUpload.getUploadDir())
                .toAbsolutePath().normalize();;
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch (Exception ex){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        this.fileRepository = fileRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public String storeFile(MultipartFile file, Long postId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        try{
            // check if the file 's name contains invalid characters
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            fileName = StringUtils.cleanPath("Post" + post.getId().toString() +"-" + timestamp.getTime() + fileName.substring(fileName.lastIndexOf("."))) ;
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, Long postId) {
        String fileName = storeFile(file, postId);
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        FileDTO fileDTO = new FileDTO(fileName , file.getContentType() ,targetLocation.toString());
        FileUpload fileUpload = mapToEntity(fileDTO);
        fileUpload.setPost(post);
        fileRepository.save(fileUpload);
        return mapToDTO(fileUpload);

    }

    @Override
    public List<FileDTO> uploadMultipleFiles(MultipartFile[] files , Long postId) {
        return Arrays.asList(files).stream().map(file -> {
            return uploadFile(file, postId);
        }).collect(Collectors.toList());
    }

    private FileUpload mapToEntity(FileDTO fileDTO){
        return mapper.map(fileDTO, FileUpload.class);
    }

    private FileDTO mapToDTO(FileUpload fileUpload){
        return mapper.map(fileUpload, FileDTO.class);
    }
}
