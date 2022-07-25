package com.miniproject.config.security;

import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.config.security.formLogin.LoginService;
import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.config.security.jwt.JwtToken;
import com.miniproject.config.security.jwt.VerifyResult;
import com.miniproject.config.security.jwt.VerifyResult.TokenStatus;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtCheckFilter extends BasicAuthenticationFilter {

    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    public JwtCheckFilter(AuthenticationManager authenticationManager, LoginService loginService, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain) throws IOException, ServletException {
        String accessHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshHeader = request.getHeader("refresh_token");
        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        String accessToken = accessHeader.substring("Bearer ".length());
        String refreshToken = refreshHeader.substring("Bearer ".length());
        VerifyResult verifyResult = jwtUtil.verifyToken(accessToken);
        if (verifyResult.getTokenStatus() == TokenStatus.ACCESS) {
            UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(verifyResult.getUsername());
            JwtToken resultToken = new JwtToken(userDetails.getAuthorities(),
                  userDetails.getUsername(), userDetails.getPassword(), true);
            SecurityContextHolder.getContext().setAuthentication(resultToken);

        } else if (verifyResult.getTokenStatus() == TokenStatus.EXPIRED) {
            VerifyResult refreshTokenVerifyResult = jwtUtil.verifyToken(refreshToken);
            if (refreshTokenVerifyResult.getTokenStatus() == TokenStatus.ACCESS) {
                String newRefreshToken = jwtUtil.reIssueRefreshToken(
                      refreshTokenVerifyResult.getUsername(),
                      refreshToken);

                response.setHeader("refresh_token", "Bearer " + newRefreshToken);
                UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(
                      refreshTokenVerifyResult.getUsername());
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.issueAccessToken(
                      userDetails.getUsername()));

                JwtToken resultToken = new JwtToken(userDetails.getAuthorities(),
                      userDetails.getUsername(), userDetails.getPassword(), true);
                SecurityContextHolder.getContext().setAuthentication(resultToken);
            } else {
                throw new AuthenticationException("Token is not valid");
            }
        } else {
            log.info("no valid JWT token found. uri = {}", request.getRequestURI());
        }
        chain.doFilter(request, response);
    }
}
