package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class BlogResponseDto {          // 게시글 조회 요청에 대한 응답으로 사용되는 DTO
    private Long id;                    // 게시글 고유 id
    private String username;            // 게시글 작성자
    private String title;               // 게시글 제목
    private String contents;            // 게시글 내용
    private LocalDateTime createAt;     // 작성일
    private LocalDateTime modifiedAt;   // 수정일

    public BlogResponseDto(Blog blog) {
        this.id = blog.getId();
        this.username = blog.getUser().getUsername();
        this.title = blog.getTitle();
        this.contents = blog.getContents();
        this.createAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
    }
}
