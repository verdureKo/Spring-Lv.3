package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlogResponseDto {
// @Setter
// @NoArgsConstructor
// param 형식으로 값을 넘겨줄땐 있어야 함
// @JsonInclude(JsonInclude.NON_NULL) null값이 아닌 것만 반환
    @Getter
    public static class CommonResponseDto {
        private Long id;
        private String title;
        private String username;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public CommonResponseDto(Blog blog) {
            this.id = blog.getId();
            this.title = blog.getTitle();
            this.username = blog.getUsername();
            this.content = blog.getContent();
            this.createdAt = blog.getCreatedAt();
            this.modifiedAt = blog.getModifiedAt();
        }
    }

    @Getter
    public static class ReadResponseDto {
        private String title;
        private String username;
        private String content;
        private LocalDateTime createdAt;

        private List<CommentResponseDto> commentList  = new ArrayList<>();

        public ReadResponseDto(Blog blog) {
            this.title = blog.getTitle();
            this.username = blog.getUsername();
            this.content = blog.getContent();
            this.createdAt = blog.getCreatedAt();
            for(Comment comment : blog.getCommentList()) {
                commentList.add(new CommentResponseDto(comment));
            }
        }
    }
}
