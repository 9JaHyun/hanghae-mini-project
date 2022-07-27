package com.miniproject.config.security.handler;

import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.config.security.jwt.VerifyResult;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Primary
@Component
public class RedisLogoutHandler implements LogoutHandler {

    private final JWTUtil jwtUtil;

    @Value("${jwt.access-expired-time}")
    private String ACCESS_TOKEN;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisLogoutHandler(JWTUtil jwtUtil, RedisTemplate<String, Object> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION)
              .substring("Bearer ".length());
        VerifyResult verifyResult = jwtUtil.verifyToken(accessToken);
        String username = verifyResult.getUsername();
        try {
            if (redisTemplate.opsForValue().get(username) != null) {
                //delete refresh token
                redisTemplate.delete(username);
            }
        } catch (IllegalArgumentException e) {
            log.warn("user does not exist");
            throw new IllegalArgumentException(e.getMessage());
        }

        //cache logout token for 10 minutes!
        redisTemplate.opsForValue().set(accessToken, true);
        redisTemplate.expire(accessToken, Long.parseLong(ACCESS_TOKEN), TimeUnit.MILLISECONDS);
    }
}
