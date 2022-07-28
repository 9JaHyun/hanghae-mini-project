package com.miniproject.common.auditing;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseEntity를 상속할 때 createdDate,modifiedDate 도 컬럼으로 인식하도록 한다.

// BaseEntity에 Auditing 기능을 포함 시킨다.
// @EntityListeners 는 리스너 클래스 지정을 통해 엔티티 객체 상태가 변경될 때 해당 리스너로 콜백을 받는다.
// AuditingEntityListener 는 JPA 내부에서 엔티티 객체가 생성/변경 되는 것을 감지한다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Column(updatable = false) @CreatedDate private LocalDateTime createdAt;
    @Column(updatable = false) @CreatedBy private Long createdBy;
    @Column(insertable = false) @LastModifiedDate private LocalDateTime updatedAt;
    @Column(insertable = false) @LastModifiedBy private Long updatedBy;

}