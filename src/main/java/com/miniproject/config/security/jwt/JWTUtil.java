package com.miniproject.config.security.jwt;

public interface JWTUtil {

    String issueAccessToken(String username);

    String issueRefreshToken(String username);

    VerifyResult verifyToken(String token);

}
