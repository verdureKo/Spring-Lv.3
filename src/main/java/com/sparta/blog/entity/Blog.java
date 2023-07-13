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

    // 지연로딩의 이유는 성능 개선에 목적이 있다.
    // `default`인 `EAGER`로 설정한다면, `Blog`를 조회할 때마다 항상 연관된 `User`도 함께 로딩되어 데이터 접근 속도는 빠르지만, 성능 저하의 가능성이 있으므로 신중하게 결정할 것
    // `FetchType.LAZY`는 `User`가 필요한 시점에만 데이터베이스에서 로딩된다. 관계가 복잡하고 관계가 많은 엔티티인 경우 유용하다.
    // `User`를 로딩하지 않고 `Blog`만 조회 가능하다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)    // 참조 무결성 Cascade 옵션
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
