package com.example.demo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BlogAPIException  extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public BlogAPIException(String message ,HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}

