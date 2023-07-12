package com.sparta.blog.entity;

import com.sparta.blog.dto.BlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter // DB와 직결되어있는 Entity클래스는 주의해 사용
@Table(name = "blogs") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Blog extends Timestamped { // 타임스탬프는 블로그 Entity에 상속되어 사용
    @Id // 해당 컬럼을 기본 키(PK)로 사용함
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // strategy 옵션사용 JPA가 기본 키로 데이터 생성 함
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "contents", nullable = false)  // 옵션에 제약 가능
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)  // N:1 관계 설정(블로그 여러개가 유저 하나에 맵핑), 게으른 삼촌은 필요한 순간 가져옴
    @JoinColumn(name = "user_id",referencedColumnName = "id")  // 외래 키(FK) 지정(컬럼명, 참조 컬럼)
    private User user;

    public Blog(BlogRequestDto requestDto, User user) {   // Blog Entity의 생성자(객체를 생성시 초기화 담당), 새로운 Blog 객체를 생성 및 저장
        this.username = user.getUsername();
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(BlogRequestDto requestDto) { // Blog Entity의 필드 값을 해당 객체의 필드 값으로 변경(수정)
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}