package com.example.demo.posts ;


import com.example.demo.exception.BlogAPIException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.posts.dto.PostDto;
import com.example.demo.tags.Tag;
import com.example.demo.tags.TagRepository;
import com.example.demo.users.User;
import com.example.demo.users.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository, UserRepository userRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto , String username) {
        Tag tag =  tagRepository.findById(postDto.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Tag" , "id" , postDto.getTagId()));
        Post post = mapToEntity(postDto);
        post.setTag(tag);
        User author = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , username));
        post.setAuthor(author);
        Post newPost = postRepository.save(post);
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo , pageSize , sort);

        Page<Post> posts = postRepository.findAll(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }


    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        System.out.println(post);
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(long id, PostDto postDto, String name) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , name));
        if(post.getAuthor().getId() != user.getId()){
            throw new BlogAPIException("You are not authorized to update this post", HttpStatus.UNAUTHORIZED);
        }
        Tag tag = tagRepository.findById(postDto.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Tag" , "id", id));
        post = mapToEntity(postDto);
        post.setTag(tag);
        postRepository.save(post);
        return mapToDTO(post);
    }

    @Override
    public void deletePostById(long id, String name) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        User user = userRepository.findByUsername(name).orElseThrow(() -> new ResourceNotFoundException("User" , "username" , name));
        if(post.getAuthor().getId() != user.getId() && !user.getRole() .equals("ADMIN")){
            throw new BlogAPIException("You are not authorized to delete this post", HttpStatus.UNAUTHORIZED);
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag" , "id" , tagId));
        List<Post> posts = postRepository.findByTagId(tagId);

        return posts.stream().map((post) -> mapToDTO(post)).collect(Collectors.toList());
    }

    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return post;
    }

    private PostDto mapToDTO(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;
    }
}
