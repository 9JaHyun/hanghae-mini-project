package com.miniproject.config.security.jwt;

import com.miniproject.config.security.RefreshTokenRepository;
import com.miniproject.config.security.domain.RefreshToken;
import com.miniproject.config.security.jwt.VerifyResult.TokenStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class JWTUtil {

    private final String SECRET_KEY;

    private final String ACCESS_EXPIRED_TIME;

    private final String REFRESH_EXPIRED_TIME;

    private final RefreshTokenRepository refreshTokenRepository;



    public JWTUtil(
          @Value("${jwt.secret-key}") String SECRET_KEY,
          @Value("${jwt.access-expired-time}") String ACCESS_EXPIRED_TIME,
          @Value("${jwt.refresh-expired-time}") String REFRESH_EXPIRED_TIME,
          RefreshTokenRepository refreshTokenRepository) {
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_EXPIRED_TIME = ACCESS_EXPIRED_TIME;
        this.REFRESH_EXPIRED_TIME = REFRESH_EXPIRED_TIME;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String issueAccessToken(String username) {
        return Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(ACCESS_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact();
    }

    @Transactional
    public String issueRefreshToken(String username) {
        RefreshToken refreshToken = createRefreshToken(username);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    private RefreshToken createRefreshToken(String username) {
        return RefreshToken.createToken(username, Jwts.builder()
              .setSubject(username)
              .setExpiration(
                    new Date(System.currentTimeMillis() + Long.parseLong(REFRESH_EXPIRED_TIME)))
              .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
              .compact());
    }

    @Transactional
    public String reIssueRefreshToken(String username, String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException(
                    username + "'s refresh token not found"));

        log.info("try to reIssue token -> {}", refreshToken);
        log.info("stored refresh token -> {}", token.getToken());

        if (refreshToken.equals(token.getToken())) {
            RefreshToken newToken = createRefreshToken(username);
            token.changeToken(newToken.getToken());
            return newToken.getToken();
        } else {
            throw new JwtException("Refresh Token does not match!");
        }
    }

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
