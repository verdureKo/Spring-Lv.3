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

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        Blog blog = findBlog(id);
        Comment comment = commentRepository.save(new Comment(requestDto, user, blog));

        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity<ApiResponse> updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        if (!validateUserOwnership(comment, user)) {
            ApiResponse apiResponse = new ApiResponse(400, "본인만 수정 가능합니다.");
            return ResponseEntity.badRequest().body(apiResponse);
        }

        comment.updateComment(requestDto);
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        ApiResponse apiResponse = new ApiResponse(200, "댓글 수정 완료!!");
        return ResponseEntity.ok().body(apiResponse);
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (!validateUserOwnership(comment, user)) {
            ApiResponse apiResponse = new ApiResponse(400, "본인만 삭제할 수 있습니다.");
            return ResponseEntity.badRequest().body(apiResponse);
        }

        commentRepository.delete(comment);
        ApiResponse apiResponse = new ApiResponse(200, "댓글 삭제 완료!!");
        return ResponseEntity.ok().body(apiResponse);
    }

    // 게시글 존재 확인 로직
    private Blog findBlog(Long id) {
        return blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    // 댓글 존재 확인 로직
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );
    }

    // 작성자 확인 로직
    private boolean validateUserOwnership(Comment comment, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        return userRoleEnum != UserRoleEnum.USER || Objects.equals(comment.getUser().getId(), user.getId());
    }
}
