package com.blog.controller;

import com.blog.payload.CommentDto;
import com.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
  private  CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto>createComment(@PathVariable(value = "postId")long postId,
                                                   @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }
    //http://localhost:8080/api/posts/2/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId")long postId){
       return commentService.getCommentsByPostId(postId);

    }
    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto>getCommentById(@PathVariable(value = "postId")long postId,@PathVariable
            (value = "id")long commentId){
        CommentDto dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/posts/1/comments/1
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto>updateComment(@RequestBody CommentDto commentDto,
                                                   @PathVariable(value = "postId")long postId,
                                                   @PathVariable(value = "commentId")long commentId){
        CommentDto dto = commentService.updateComment(commentId, postId, commentDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    //http://localhost:8080/api/posts/1/comments/1
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String>deleteComment(@PathVariable(value = "postId")long postId,
                                               @PathVariable(value = "commentId")long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK);
    }

}
