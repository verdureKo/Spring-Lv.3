package com.sparta.blog.dto;

import lombok.Getter;

@Getter
public class BlogRequestDto {    // blog Entity 생성시 필요한 정보 DTO
    private String title;        // 제목
    private String contents;     // 내용
}