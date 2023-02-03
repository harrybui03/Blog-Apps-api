package com.example.demo.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentDto commentDto , @PathVariable(value = "postId") long postId , Authentication authentication){
        return new ResponseEntity<>(commentService.createComment(commentDto , postId , authentication.getName()) , HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllCommentsByPostId(@PathVariable(value = "postId") long postId){
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") long postId , @PathVariable(value = "id") long id){
        return ResponseEntity.ok(commentService.getCommentById(id , postId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long postId , @PathVariable(value = "id") long id , @RequestBody @Valid CommentDto commentDto , Authentication authentication){
        return ResponseEntity.ok(commentService.updateComment(id , postId , commentDto , authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId , @PathVariable(value = "id") long id , Authentication authentication){
        commentService.deleteComment(postId , id , authentication.getName());
        return new ResponseEntity<>("Comment deleted successfully" , HttpStatus.OK);
    }
}
