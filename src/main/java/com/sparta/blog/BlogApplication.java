package com.sparta.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling	 											  // 스케줄러 작동 Annotation
@EnableJpaAuditing												  // 시간 자동변경 Annotation
@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // Spring Security 인증 기능 제외 옵션
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}
