package com.miniproject.config.security.domain;

import com.miniproject.user.service.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private TokenBox tokenBox;
    private UserInfoDto userInfoDto;
}
