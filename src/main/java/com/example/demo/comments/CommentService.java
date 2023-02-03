package com.example.demo.comments;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto , long postId , String username);

    List<CommentDto> getAllCommentsByPostId(long postId);

    CommentDto getCommentById(long id , long postId);

    CommentDto updateComment(long id , long postId , CommentDto commentDto, String name);

    void deleteComment(long postId, long id, String name);
}
