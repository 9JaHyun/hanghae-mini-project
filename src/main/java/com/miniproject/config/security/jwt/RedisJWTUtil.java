package com.miniproject.config.security.jwt;

import com.miniproject.config.security.domain.RefreshTokenRedis;
import com.miniproject.config.security.jwt.VerifyResult.TokenStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
//@Primary
public class RedisJWTUtil implements JWTUtil{

    private final String SECRET_KEY;
    private final String ACCESS_EXPIRED_TIME;
    private final String REFRESH_EXPIRED_TIME;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisJWTUtil(
          @Value("${jwt.secret-key}") String SECRET_KEY,
          @Value("${jwt.access-expired-time}") String ACCESS_EXPIRED_TIME,
          @Value("${jwt.refresh-expired-time}") String REFRESH_EXPIRED_TIME,
          RedisTemplate<String, Object> redisTemplate) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String issueAccessToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(ACCESS_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    @Override
    @Transactional
    public String issueRefreshToken(String username) {
        RefreshTokenRedis refreshToken = createRefreshToken(username);
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(username, refreshToken.getToken());
        return refreshToken.getToken();
    }

    private RefreshTokenRedis createRefreshToken(String username) {
        return RefreshTokenRedis.createToken(username, Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(REFRESH_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact());
    }

    @Override
    public VerifyResult verifyToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
                  .getBody();
            return new VerifyResult(claims.getSubject(), TokenStatus.ACCESS);
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            log.info("JWT is Expired -> {}", e.getMessage());
            return new VerifyResult(null, TokenStatus.EXPIRED);
        } catch (Exception e) { // 그외 에러났을 경우
            log.info("JWT has Exception -> {}", e.getMessage());
            return new VerifyResult(null, TokenStatus.DENIED);
        }

    }
}
