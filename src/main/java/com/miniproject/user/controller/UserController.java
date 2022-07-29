package com.miniproject.user.controller;

import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(HttpServletRequest request, SignUpRequestDto dto) {
        log.info("dto list -> {}", dto);
        userService.signUp(extractDomainRoot(request), dto);

        return ResponseEntity
              .status(HttpStatus.CREATED)
              .body("회원가입 완료!");
    }

    private String extractDomainRoot(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI().toString(), "");
    }

    @GetMapping("/certification")
    public ResponseEntity<String> requestCode(String email, String authKey) {
        userService.verityEmail(email, authKey);
        return ResponseEntity
              .status(HttpStatus.OK)
              .body("인증 완료!");
    }
}
