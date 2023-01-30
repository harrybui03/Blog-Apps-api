package com.example.demo.tags;

import com.example.demo.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    private final ModelMapper mapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }


    @Override
    public TagDto createTag(TagDto tagDto) {
        Tag tag = mapToEntity(tagDto);
        tagRepository.save(tag);
        return mapToDTO(tag);
    }

    @Override
    public TagDto getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag" , "Id" , tagId));
        return mapToDTO(tag);
    }

    @Override
    public List<TagDto> getAllTags() {
        List<Tag> listTags = tagRepository.findAll();
        List<TagDto> listTagsDto = new ArrayList<>();
        for(Tag tag : listTags){
            listTagsDto.add(mapToDTO(tag));
        }
        return listTagsDto;
    }

    @Override
    public TagDto updateTag(TagDto tagDto, Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag" , "Id" , tagId));
        tag = mapToEntity(tagDto);
        tagRepository.save(tag);
        return mapToDTO(tag);
    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag" , "Id" , tagId));
        tagRepository.delete(tag);
    }

    private Tag mapToEntity(TagDto tagDto){
        Tag tag = mapper.map(tagDto, Tag.class);
        return tag;
    }

    private TagDto mapToDTO(Tag tag){
        TagDto tagDto = mapper.map(tag, TagDto.class);
        return tagDto;
    }
}
