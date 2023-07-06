package com.blog.service;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.exception.ResourceNotFoundException;
import com.blog.payload.CommentDto;
import com.blog.repository.CommentRepository;
import com.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:" + postId)
        );
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        CommentDto dto = mapToDto(newComment);
        return dto;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:"+postId)
        );
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment->mapToDto(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id:" + postId)
        );

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found with id:" +commentId)
        );
       return mapToDto(comment);

    }

    @Override
    public CommentDto updateComment(long commentId, long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Comment not found with id:" + commentId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:" +commentId)
        );
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment comments = commentRepository.save(comment);
        return mapToDto(comments);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post not found with id:" + postId)
        );
        Comment comments = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment not found with id:" + commentId)
        );
        commentRepository.deleteById(commentId);
    }

    CommentDto mapToDto(Comment comment){
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
       /* CommentDto commentDto=new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());*/
        return commentDto;

    }
    Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
       /* Comment comment=new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());*/
        return comment;
    }
}
