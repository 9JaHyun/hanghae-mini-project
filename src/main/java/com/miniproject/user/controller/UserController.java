package com.miniproject.user.controller;

import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public ResponseEntity<String> signUp(SignUpRequestDto dto) {
        userService.signUp(dto);
        return ResponseEntity
              .status(HttpStatus.CREATED)
              .body("회원가입 완료!");
    }

}
