package com.example.demo.posts ;


import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.tags.Tag;
import com.example.demo.tags.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    private final TagRepository tagRepository;
    private final ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper,TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.tagRepository = tagRepository;
    }


    @Override
    public PostDto createPost(PostDto postDto) {
        Tag tag =  tagRepository.findById(postDto.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Tag" , "id" , postDto.getTagId()));
        Post post = mapToEntity(postDto);
        post.setTag(tag);
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
    public PostDto updatePost(long id,PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        Tag tag = tagRepository.findById(postDto.getTagId()).orElseThrow(() -> new ResourceNotFoundException("Tag" , "id", id));
        post = mapToEntity(postDto);
        post.setTag(tag);
        postRepository.save(post);
        return mapToDTO(post);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
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
