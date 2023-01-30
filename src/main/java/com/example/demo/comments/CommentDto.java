package com.example.demo.comments;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class CommentDto {
    private long id;
    private long userId;
    @NotEmpty(message = "Body is required")
    private String body;

}
