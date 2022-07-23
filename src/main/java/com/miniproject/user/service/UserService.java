package com.miniproject.user.service;

import com.miniproject.user.dto.SignUpRequestDto;

public interface UserService {

    void signUp(SignUpRequestDto dto);
}
