package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.result.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public List<BlogResponseDto.ReadResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc().stream().map(BlogResponseDto.ReadResponseDto::new).toList();
    }

    public BlogResponseDto.CommonResponseDto createBlog(BlogRequestDto requestDto, User user) {
        Blog blog = blogRepository.save(new Blog(requestDto, user));
        return new BlogResponseDto.CommonResponseDto(blog);
    }

    public BlogResponseDto.ReadResponseDto getBlog(Long id) {
        Blog blog = findBlog(id);
        return ResponseEntity.ok().body(new BlogResponseDto.ReadResponseDto(blog)).getBody();
    }

    @Transactional
    public BlogResponseDto.ReadResponseDto updateBlog(Long id, BlogRequestDto requestDto, User user) {
        Blog blog = findBlog(id);
        confirmUser(blog, user);
        blog.updateBlog(requestDto);
        return ResponseEntity.ok().body(new BlogResponseDto.ReadResponseDto(blog)).getBody();
    }

    public ResponseEntity<ApiResponse> deleteBlog(Long id, User user){
        ApiResponse apiResponse = new ApiResponse();
        
        Blog blog = findBlog(id);
        confirmUser(blog, user);
        
        blogRepository.delete(blog);
        apiResponse.setMessage("삭제 완료!!");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    private Blog findBlog(Long id){
        return blogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    private void confirmUser(Blog blog, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER && !Objects.equals(blog.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글이 아닙니다.");
        }
    }

}