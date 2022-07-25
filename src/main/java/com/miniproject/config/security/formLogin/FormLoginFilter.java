package com.miniproject.config.security.formLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.user.dto.LoginRequestDto;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FormLoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
          HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginDto = objectMapper.readValue(request.getInputStream(),
                  LoginRequestDto.class);

            UsernamePasswordAuthenticationToken token =
                  new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    protected void successfulAuthentication(HttpServletRequest request,
          HttpServletResponse response, FilterChain chain, Authentication authResult)
          throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        response.setHeader(HttpHeaders.AUTHORIZATION,
              "Bearer " + jwtUtil.issueAccessToken(userDetails.getUsername()));
        String refreshToken = jwtUtil.issueRefreshToken(userDetails.getUsername());
        response.setHeader("refresh_token", "Bearer " + refreshToken);
    }
}
