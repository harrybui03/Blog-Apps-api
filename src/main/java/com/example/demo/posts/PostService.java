package com.example.demo.posts ;


import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto , String token);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(long id,PostDto postDto );

    void deletePostById(long Id);

    List<PostDto> getPostsByTag(Long tagId);

}
