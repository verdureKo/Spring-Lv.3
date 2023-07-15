package com.sparta.blog.controller;

import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// ResponseEntity<?>에서 물음표(?)는 제네릭 타입의 와일드카드(Wildcard)를 나타냅니다.
// 와일드카드는 타입 매개변수에 대한 불확실성을 나타내며, 다양한 타입을 수용할 수 있는 유연성을 제공한다.
@RestController
@RequestMapping("/api/logs")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.createComment(boardId, requestDto, userDetails.getUser()));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }
}
