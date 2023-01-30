package com.example.demo.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")

public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto){
        return new ResponseEntity<>(tagService.createTag(tagDto) , HttpStatus.CREATED) ;
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagDto> getTag(@PathVariable("tagId") Long tagId){
        return ResponseEntity.ok(tagService.getTag(tagId));
    }

    @GetMapping
    public List<TagDto> getAllTags(){
        return tagService.getAllTags();
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagDto> updateTag(@RequestBody TagDto tagDto , @PathVariable("tagId") Long tagId){
        return new ResponseEntity<>(tagService.updateTag(tagDto , tagId) , HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable("tagId") Long tagId){
        tagService.deleteTag(tagId);
        return new ResponseEntity<>("Tag is deleted successfully" , HttpStatus.OK);
    }
}
