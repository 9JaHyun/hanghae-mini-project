package com.miniproject.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false, unique = true) private String authKey;

    public EmailAuth(String username, String authKey) {
        this.username = username;
        this.authKey = authKey;
    }
}
