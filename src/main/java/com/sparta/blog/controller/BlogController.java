package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.result.ApiResponse;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor

public class BlogController {
    private final BlogService blogService;

    @GetMapping("/logs")
    public List<BlogResponseDto.ReadResponseDto> getBlogs(){
        return blogService.getBlogs();
    }

    @PostMapping("/logs")
    public BlogResponseDto.CommonResponseDto createBlog(@RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return blogService.createBlog(requestDto, userDetails.getUser());
    }

    @GetMapping("/logs/{id}")       // 튜터님 피드백: 메소드명으로 선택조회를 유추할 수 있음, API URI convention에 따라 복수 사용
    public BlogResponseDto.ReadResponseDto getBlog(@PathVariable Long id){
        return blogService.getBlog(id);
    }


    @PutMapping("/logs/{id}")
    public BlogResponseDto.ReadResponseDto updateBlog(
            @PathVariable Long id, @RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return blogService.updateBlog(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/logs/{id}")
    public ResponseEntity<ApiResponse> deleteBlog(
            @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return blogService.deleteBlog(id, userDetails.getUser());
    }
}
