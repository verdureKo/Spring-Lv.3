package com.sparta.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig {

    @Bean  // 수동 등록 된 Bean = 기술 지원 Bean : 기술적인 문제, 공통 관심사 처리시 사용, 위치 파악 easy
    public PasswordEncoder passwordEncoder() {  // @Bean 설정된 메서드 호출
        return new BCryptPasswordEncoder();  // passwordEncoder -> Spring IoC 컨테이너
    }

}