package com.miniproject.user.service;

import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.dto.UserInfoDto;

public interface UserService {

    void signUp(String email, SignUpRequestDto dto);

    UserInfoDto sendUserInfo(String username);

    boolean checkValidUser(String username);

    void createCertificationCode(String email, String url);

    void verityEmail(String email, String authKey);
}
