package com.example.demo.tags.dto;

import com.example.demo.posts.Post;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class TagDto {
    private Long id;

    @NotEmpty
    @Size(min = 2)
    private String name;

    @NotEmpty
    @Size(min = 2)
    private String description;

    private List<Post> posts;
}
