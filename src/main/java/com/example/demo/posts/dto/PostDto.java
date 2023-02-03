package com.example.demo.posts.dto;

import com.example.demo.uploadFile.FileDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostDto {

    private long id;

    @NotEmpty
    @Size(min = 2 , message = "Post title should have at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 100 , message = "Post body should have at least 100 characters")
    private String body;

    @NotEmpty
    @Size(min = 2 , message = "Post titleURL should have at least 2 characters")
    private String titleURL;

    private List<FileDTO> images;


    private Long tagId;
}
