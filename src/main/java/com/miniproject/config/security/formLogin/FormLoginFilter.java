package com.miniproject.config.security.formLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniproject.config.security.domain.LoginRequestDto;
import com.miniproject.config.security.domain.LoginResponseDto;
import com.miniproject.config.security.domain.TokenBox;
import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.user.service.UserInfoDto;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@CrossOrigin
public class FormLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FormLoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super();
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
          HttpServletResponse response) throws AuthenticationException {

        try {
            log.info("Get Login information: ");
            LoginRequestDto loginDto = objectMapper.readValue(request.getInputStream(),
                  LoginRequestDto.class);

            UsernamePasswordAuthenticationToken token =
                  new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            return getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 로그인 정보입니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
          HttpServletResponse response, FilterChain chain, Authentication authResult)
          throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String accessToken = jwtUtil.issueAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtil.issueRefreshToken(userDetails.getUsername());

        UserInfoDto userInfoDto = new UserInfoDto(userDetails.getUser());
        LoginResponseDto loginResponseDto = new LoginResponseDto(new TokenBox(accessToken, refreshToken), userInfoDto);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(loginResponseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
          HttpServletResponse response, AuthenticationException failed)
          throws IOException, ServletException {
        log.info("failed error -> {}", failed.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}
