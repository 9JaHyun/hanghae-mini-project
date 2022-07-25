package com.miniproject.config.security.jwt;

public interface JWTUtil {

    String issueAccessToken(String username);

    String issueRefreshToken(String username);

    String reIssueRefreshToken(String username, String refreshToken);

    VerifyResult verifyToken(String token);

}
