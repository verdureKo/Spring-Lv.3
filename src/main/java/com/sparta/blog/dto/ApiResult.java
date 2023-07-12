package com.sparta.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ApiResult {    // 결과 반환을 위한 DTO
        private String message;  // 성공 메세지와
        private int statusCode;  // 상태 코드를 반환

    @Builder
    public ApiResult(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
