package com.miniproject.user.service;

import com.miniproject.user.dto.SignUpRequestDto;

public interface UserService {

    void signUp(SignUpRequestDto dto);

    UserInfoDto sendUserInfo(String username);

    boolean checkValidUser(String username);

    void createCertificationCode(String email, String url);

    void verityEmail(String email, String authKey);

    void validateUsername(String username);

    void validateDuplicateNickname(String nickname);
}
