package com.miniproject.config.security;

import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.config.security.jwt.JwtToken;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtCheckFilter extends BasicAuthenticationFilter {

    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    public JwtCheckFilter(AuthenticationManager authenticationManager, LoginService loginService,
          JWTUtil jwtUtil) {
        super(authenticationManager);
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null || !accessToken.startsWith("access_token ")) {
            chain.doFilter(request, response);
            return;
        }
        String token = accessToken.substring("access_token ".length());
        String username = jwtUtil.verifyToken(token);
        if (username != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(
                  username);
            JwtToken resultToken =
                  new JwtToken(userDetails.getAuthorities(), userDetails.getUsername(), userDetails.getPassword(), true);
            SecurityContextHolder.getContext().setAuthentication(resultToken);
            chain.doFilter(request, response);
        } else {
            throw new AuthenticationException("Token is not valid");
        }
    }
}
