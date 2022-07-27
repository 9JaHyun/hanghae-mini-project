package com.miniproject.config.security.handler;

import com.miniproject.config.security.domain.RefreshToken;
import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.config.security.jwt.VerifyResult;
import com.miniproject.config.security.repository.RefreshTokenRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class RdbLogoutHandler implements LogoutHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public RdbLogoutHandler(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION)
              .substring("Bearer ".length());
        VerifyResult verifyResult = jwtUtil.verifyToken(accessToken);
        String username = verifyResult.getUsername();
        try {
            RefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                  .orElseThrow(() -> new IllegalArgumentException("이미 로그아웃 하셨습니다."));
            refreshTokenRepository.delete(refreshToken);
        } catch (IllegalArgumentException e) {
            log.warn("user does not exist");
        }
    }
}
