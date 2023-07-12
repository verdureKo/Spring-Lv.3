package com.sparta.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {    // 로그인에서 필요한 정보를 담는 DTO
    private String username;      // 이름
    private String password;      // 비밀번호
}