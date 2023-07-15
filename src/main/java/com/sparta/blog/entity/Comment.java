package com.sparta.blog.entity;

import com.sparta.blog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="comments")
@NoArgsConstructor
public class Comment extends Timestamped{   // Timestamped 상속받도록 함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    // User 컬럼도 필요시에만 로딩하도록 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Blog 컬럼도 필요시에만 로딩하도록 지연로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    public Comment(CommentRequestDto requestDto, User user, Blog blog) {
        this.comment = requestDto.getComment();
        this.user = user;
        this.blog = blog;
    }

    public void updateComment(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
