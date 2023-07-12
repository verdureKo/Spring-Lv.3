package com.sparta.blog.repository;

import com.sparta.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // repository에서 username 찾기
    boolean existsByUsername(String username);      // repository에서 username이 존재여부
}