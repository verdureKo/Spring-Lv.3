package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="blogs")
@NoArgsConstructor
public class Blog extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String content;

    // 게시글 조회시 `User`컬럼 필요할때만 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 참조 무결성 Cascade 옵션, 댓글목록은 필요시에만 로딩 (default Lazy)
    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
    @OrderBy("id desc") // 내림차순 정렬
    private List<Comment> commentList = new ArrayList<>();

    public Blog(BlogRequestDto requestDto, User user){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
    }

    public void updateBlog(BlogRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
