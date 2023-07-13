package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.result.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

// @Transactional(readOnly = true)의 사용
// 조회 작업이 주된 목적이고 데이터 변경이 없는 경우.
// 동시성이 높은 환경에서 많은 사용자가 동시에 조회 작업을 수행하는 경우.
// 데이터 일관성을 보장해야 하는 경우.
// 조회 작업 중에도 데이터 변경이 발생하거나, 데이터베이스에서 최신 상태의 데이터를 필요로 하는 경우 적용하지않는다.
// 트랜잭션을 관리하는 것은 데이터베이스 작업의 원자성(Atomicity), 일관성(Consistency), 독립성(Isolation), 지속성(Durability)을 보장한다.
// 삭제작업또한 실패할 경우 롤백하고 성공할 경우 커밋한다.
@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    //메서드의 순서: 메서드들의 순서를 일관성 있게 정렬해보세요. 일반적으로 생성(CREATE) - 조회(READ) - 수정(UPDATE) - 삭제(DELETE) 순으로 메서드를 정렬하는 것이 관례
    @Transactional
    public BlogResponseDto.ResponseDto createBlog(BlogRequestDto requestDto, User user) {
        Blog blog = blogRepository.save(new Blog(requestDto, user));
        return new BlogResponseDto.ResponseDto(blog);
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto.DetailsResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(BlogResponseDto.DetailsResponseDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public BlogResponseDto.DetailsResponseDto getBlog(Long id) {
        Blog blog = findBlog(id);
        return new BlogResponseDto.DetailsResponseDto(blog);
    }

    @Transactional
    public BlogResponseDto.DetailsResponseDto updateBlog(Long id, BlogRequestDto requestDto, User user) {
        Blog blog = findBlog(id);
        validateUserOwnership(blog, user);
        blog.updateBlog(requestDto);
        return new BlogResponseDto.DetailsResponseDto(blog);
    }

    @Transactional
    public ApiResponse deleteBlog(Long id, User user) {
        Blog blog = findBlog(id);
        validateUserOwnership(blog, user);
        blogRepository.delete(blog);
        return new ApiResponse(200, "삭제 완료!!");
        // ApiResponse 반환 변경: ResponseEntity<ApiResponse> 대신 ApiResponse를 직접 반환하도록 변경
    }

    // 게시글 존재 확인 로직
    private Blog findBlog(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }

    // 작성자 확인 로직
    private void validateUserOwnership(Blog blog, User user) {
        UserRoleEnum userRoleEnum = user.getRole();
        if (userRoleEnum == UserRoleEnum.USER && !Objects.equals(blog.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("본인이 작성한 게시글이 아닙니다.");
        }
    }
}
