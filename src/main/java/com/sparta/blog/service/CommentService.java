package com.sparta.blog.service;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.result.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor

public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;

    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 게시글이 존재하지 않습니다.")
        );
        Comment comment = commentRepository.save(new Comment(requestDto, user, blog));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if(!confirmUser(comment, user)){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("본인만 수정 가능합니다.");
            apiResponse.setStatusCode(400);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        comment.updateComment(requestDto);
        return ResponseEntity.ok().body(new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteComment(Long commentId, User user) {
        ApiResponse apiResponse = new ApiResponse();

        Comment comment = findComment(commentId);

        if(!confirmUser(comment, user)){
            apiResponse.setMessage("본인만 삭제할 수 있습니다.");
            apiResponse.setStatusCode(400);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        commentRepository.delete(comment);

        apiResponse.setMessage("삭제 성공!!");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("해당 댓글이 존재하지 않습니다.")
        );
    }

    private boolean confirmUser(Comment comment, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        return userRoleEnum != UserRoleEnum.USER || Objects.equals(comment.getUser().getId(), user.getId());
    }
}
