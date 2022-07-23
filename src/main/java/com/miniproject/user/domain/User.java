package com.miniproject.user.domain;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", indexes = {
      @Index(columnList = "username"),
      @Index(columnList = "nickname")
})
@Entity
public class User {


    @Column(nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String password;
    @Column(nullable = false, unique = true) private String nickname;
    private String profile;

    @Column(updatable = false) @CreatedDate private LocalDateTime createdAt;
    @Column(updatable = false) @CreatedBy private String createdBy;
    @Column(insertable = false) @LastModifiedDate private LocalDateTime updatedAt;
    @Column(insertable = false) @LastModifiedBy private String updatedBy;

    protected User() {
    }

    public User(String username, String password, String nickname, String profile) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
    }

}
