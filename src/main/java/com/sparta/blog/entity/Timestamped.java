package com.sparta.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // 작성, 수정 일시 쾅쾅
public abstract class Timestamped {

    @CreatedDate                                // 사용하면 자동으로 updatable = false옵션 적용
    private LocalDateTime createdAt;

    @LastModifiedDate                           // 사용하면 자동으로 updatable = true옵션 적용
    private LocalDateTime modifiedAt;
}