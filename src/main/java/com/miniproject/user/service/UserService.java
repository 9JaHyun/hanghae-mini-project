package com.miniproject.user.service;

import com.miniproject.user.dto.SignUpRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    @Transactional
    void signUp(SignUpRequestDto dto);

    void createCertificationCode(String email, String url);

    @Transactional
    void verityEmail(String email, String authKey);

    void validateUsername(String username);

    void validateDuplicateNickname(String nickname);
}
