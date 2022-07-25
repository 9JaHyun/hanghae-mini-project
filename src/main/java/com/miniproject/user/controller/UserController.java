package com.miniproject.user.controller;

import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.user.dto.CertificationCodeDto;
import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;


    public UserController(UserService userService, JWTUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto dto) {
        userService.signUp(dto);
        return ResponseEntity
              .status(HttpStatus.CREATED)
              .body("회원가입 완료!");
    }

    @GetMapping("/certification")
    public ResponseEntity<String> requestCode(String email) {
        userService.requestCertificationCode(email);
        return ResponseEntity
              .status(HttpStatus.OK)
              .body("이메일 전송 완료!");
    }

    @PostMapping("/certification")
    public ResponseEntity<String> certification(@RequestBody CertificationCodeDto dto) {
        userService.checkCertificationCode(dto);
        return ResponseEntity
              .status(HttpStatus.OK)
              .body("인증 완료!!");
    }
}
