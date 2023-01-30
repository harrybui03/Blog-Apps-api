package com.example.demo.posts ;

import com.example.demo.uploadFile.FileService;
import com.example.demo.uploadFile.FileDTO;
import com.example.demo.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    FileService fileService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED) ;
    }

    @PostMapping("/upload/{postId}")
    public ResponseEntity<List<FileDTO>> uploadImg(@RequestParam("file")MultipartFile files[], @PathVariable(name = "postId") long postId){
        return ResponseEntity.ok(fileService.uploadMultipleFiles(files , postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable(name= "postId") Integer postId){
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping()
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo" , defaultValue = AppConstants.DEFAULT_PAGE_NUMBER , required = false) int pageNo,
            @RequestParam(value = "pageSize" , defaultValue = AppConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize,
            @RequestParam(value = "sortBy" , defaultValue = AppConstants.DEFAULT_SORT_BY , required = false) String sortBy,
            @RequestParam(value = "sortDir" , defaultValue = AppConstants.DEFAULT_SORT_DIRECTION , required = false) String sortDir
            ){
        return postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "postId") long postId , @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.updatePost(postId,postDto), HttpStatus.OK);
    }

    @DeleteMapping(("/{postId}"))
    public ResponseEntity<String> deletePost(@PathVariable(name = "postId") long postId){
        postService.deletePostById(postId);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<PostDto>> getPostByTag(@PathVariable("tagId") Long tagId){
        return ResponseEntity.ok(postService.getPostsByTag(tagId));
    }


}
