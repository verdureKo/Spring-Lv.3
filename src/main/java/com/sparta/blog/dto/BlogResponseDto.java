package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// @Setter, @NoArgsConstructor : param 형식으로 값을 넘겨줄땐 있어야 함
// @JsonInclude(JsonInclude.NON_NULL) null값이 아닌 것만 반환해서 주석
public class BlogResponseDto {
// 클래스 내부에는 두 개의 중첩 클래스를 만든 이유는 클라이언트가 한 번의 요청으로
// 블로그의 상세 정보와 댓글 목록을 한꺼번에 받을 수 있게 하기 위함이다.
    @Getter
    public static class ResponseDto {
        private Long id;
        private String title;
        private String username;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public ResponseDto(Blog blog) {
            this.id = blog.getId();
            this.title = blog.getTitle();
            this.username = blog.getUsername();
            this.content = blog.getContent();
            this.createdAt = blog.getCreatedAt();
            this.modifiedAt = blog.getModifiedAt();
        }
    }

    @Getter
    public static class DetailsResponseDto {
        private String title;
        private String username;
        private String content;
        private LocalDateTime createdAt;

        private List<CommentResponseDto> commentList  = new ArrayList<>();
        //  Blog 객체를 매개변수로 받는 생성자를 가지며, 해당 블로그의 필드 값을 가져와서 초기화합니다.
        // 또한, 블로그에 작성된 댓글 목록을 CommentResponseDto로 변환하여 commentList에 추가합니다.
        public DetailsResponseDto(Blog blog) {
            this.title = blog.getTitle();
            this.username = blog.getUsername();
            this.content = blog.getContent();
            this.createdAt = blog.getCreatedAt();
            this.commentList = blog.getCommentList()
                    .stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }
    }
}
