package com.example.demo.posts ;


import com.example.demo.posts.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto , String username);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(long id, PostDto postDto, String name);

    void deletePostById(long Id, String name);

    List<PostDto> getPostsByTag(Long tagId);

}
