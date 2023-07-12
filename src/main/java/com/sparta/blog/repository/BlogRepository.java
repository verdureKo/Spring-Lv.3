package com.sparta.blog.repository;


import com.sparta.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByOrderByModifiedAtDesc(); // 게시글 조회시 수정일자 기준 내림차순 정렬
}
