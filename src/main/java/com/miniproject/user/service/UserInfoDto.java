package com.miniproject.user.service;

import com.miniproject.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserInfoDto {

    private String nickname;
    private String profile;

    public UserInfoDto(User user) {
        this.nickname = user.getNickname();
        this.profile = user.getProfile();
    }
}
