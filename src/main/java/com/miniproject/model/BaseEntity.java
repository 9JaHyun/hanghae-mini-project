package com.miniproject.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseEntity를 상속할 때 createdDate,modifiedDate 도 컬럼으로 인식하도록 한다.

// BaseEntity에 Auditing 기능을 포함 시킨다.
// @EntityListeners 는 리스너 클래스 지정을 통해 엔티티 객체 상태가 변경될 때 해당 리스너로 콜백을 받는다.
// AuditingEntityListener 는 JPA 내부에서 엔티티 객체가 생성/변경 되는 것을 감지한다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate // 생성일 자동 저장
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate // 마지막 수정시간 자동 저장
    private LocalDateTime modifiedDate;
}