package com.example.demo.tags;

import com.example.demo.tags.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto createTag(TagDto tagDto);

    TagDto getTag(Long tagId);

    List<TagDto> getAllTags();

    TagDto updateTag(TagDto tagDto , Long tagId);

    void deleteTag(Long tagId);
}
