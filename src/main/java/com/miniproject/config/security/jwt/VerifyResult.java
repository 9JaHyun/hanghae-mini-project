package com.miniproject.config.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResult {

    private String username;
    private TokenStatus tokenStatus;


    public static enum TokenStatus {
        ACCESS,
        EXPIRED,
        DENIED
    }
}
