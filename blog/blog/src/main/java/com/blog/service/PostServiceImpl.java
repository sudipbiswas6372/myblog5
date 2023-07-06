package com.blog.service;

import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.PostDto;
import com.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
   private PostRepository postRepository;

  private ModelMapper modelMapper;


    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        // can't save i Dto convert Dto to Entity
        Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post newPost=postRepository.save(post);

        //now converting back Entity to Dto bec return type is Dto
        PostDto dto=new PostDto();
        dto.setId(newPost.getId());
        dto.setTitle(newPost.getTitle());
        dto.setDescription(newPost.getDescription());
        dto.setContent(newPost.getContent());
        return dto;
    }

    @Override
    public List<PostDto> listAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
       // Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> listOfposts = postRepository.findAll(pageable);
        List<Post> posts = listOfposts.getContent();
        List<PostDto>postDto = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        return postDto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("post not found with id:"+id)
        );
       return mapToDto(post);

    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:" + id)
        );
        Post newPost = mapToEntity(postDto);
        newPost.setId(id);
        Post updatePost = postRepository.save(newPost);
        return mapToDto(updatePost);

    }

    @Override
    public void deleteById(long id) {
        postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Post not found with id:"+id)
        );
        postRepository.deleteById(id);
    }

    PostDto mapToDto(Post post){
       PostDto dto = modelMapper.map(post, PostDto.class);
        /*PostDto dto=new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());*/
        return dto;
    }
    Post mapToEntity(PostDto postDto){
       Post post = modelMapper.map(postDto, Post.class);
        /*Post post=new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/
        return post;
    }
}
