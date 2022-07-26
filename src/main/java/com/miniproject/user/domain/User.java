package com.miniproject.user.domain;


import com.miniproject.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users", indexes = {
      @Index(columnList = "username"),
      @Index(columnList = "nickname")
})
@Entity
public class User extends BaseEntity {


    @Column(nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Setter @Column(nullable = false) private String password;
    @Column(nullable = false, unique = true) private String nickname;
    private String profile;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    protected User() {
    }

    public User(String username, String password, String nickname, String profile) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.userStatus = UserStatus.NOT_VALID;
    }

    public void changeStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

}
