package com.miniproject.user.dto;

import com.miniproject.user.domain.User;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public final class SignUpRequestDto implements Serializable {

    @Email @NotBlank final String username;
    @NotBlank final String password;
    @NotBlank final String rePassword;
    @NotBlank final String nickname;
    final String profile;

    public SignUpRequestDto(String username, String password, String rePassword, String nickname,
          String profile) {
        this.username = username;
        this.password = password;
        this.rePassword = rePassword;
        this.nickname = nickname;
        this.profile = profile;
    }

    public User toEntity() {
        return new User(username, password, nickname, profile);
    }
}
