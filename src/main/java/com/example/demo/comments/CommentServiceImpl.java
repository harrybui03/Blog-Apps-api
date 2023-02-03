package com.example.demo.comments;

import com.example.demo.exception.BlogAPIException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.posts.Post;
import com.example.demo.posts.PostRepository;
import com.example.demo.users.Roles;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public CommentDto createComment(CommentDto commentDto, long postId ,String username) {
        Comment comment = mapToEntity(commentDto);

        // retrieve the post
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" , postId));

        // retrieve the user
        User author = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , username));
        // set the post to the comment
        comment.setPost(post);
        // set the user to the comment
        comment.setUser(author);

        // save to database
        commentRepository.save(comment);

        return mapToDto(comment);
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findBypostId(postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long id , long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" , postId));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment" , "id" , id));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to this post" , HttpStatus.BAD_REQUEST);
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long id, long postId, CommentDto commentDto, String name) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" , postId));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment" , "id" , id));

        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , name));

        if(user.getId() != comment.getUser().getId()){
            throw new BlogAPIException("You are not authorized to update this comment" , HttpStatus.UNAUTHORIZED);
        }

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to this post" , HttpStatus.BAD_REQUEST);
        }

        comment.setBody(commentDto.getBody());

        commentRepository.save(comment);

        return mapToDto(comment);
    }

    @Override
    public void deleteComment(long postId, long id, String name) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post" , "id" , postId));

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment" , "id" , id));

        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , name));

        if(user.getId() != comment.getUser().getId() && user.getRole() != Roles.ADMIN){
            throw new BlogAPIException("You are not authorized to delete this comment" , HttpStatus.UNAUTHORIZED);
        }

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException("Comment does not belong to this post" , HttpStatus.BAD_REQUEST);
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        return modelMapper.map(comment , CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto){
        return modelMapper.map(commentDto , Comment.class);
    }
}
