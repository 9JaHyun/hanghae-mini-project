package com.miniproject.config.security.jwt;

import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.config.security.formLogin.LoginService;
import com.miniproject.config.security.jwt.VerifyResult.TokenStatus;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class JwtCheckFilter extends BasicAuthenticationFilter {

    private final LoginService loginService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtil jwtUtil;

    public JwtCheckFilter(AuthenticationManager authenticationManager, LoginService loginService,
          RedisTemplate<String, Object> redisTemplate, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.loginService = loginService;
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain chain) throws IOException, ServletException {
        String accessHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accessHeader == null || !accessHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = accessHeader.substring("Bearer ".length());

        VerifyResult verifyResult = jwtUtil.verifyToken(accessToken);
        if (verifyResult.getTokenStatus() == TokenStatus.ACCESS) {
//            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
//            if (operations.get(accessToken) != null && (boolean) operations.get(accessToken)) {
//                log.info("invalid token! (blacklist token)");
//                chain.doFilter(request, response);
//                return;
//            }
            UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(verifyResult.getUsername());

            UsernamePasswordAuthenticationToken resultToken = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(resultToken);
        } else if (verifyResult.getTokenStatus() == TokenStatus.EXPIRED) {
            String refreshToken = extractTokenFromHeader(request);

            // 반드시 만료된 토큰이 있는 상태에서 refresh_token 이 있어야 함.
            VerifyResult refreshTokenVerifyResult = jwtUtil.verifyToken(refreshToken);
            if (refreshTokenVerifyResult.getTokenStatus() == TokenStatus.ACCESS) {
                UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(
                      refreshTokenVerifyResult.getUsername());
                response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "
                      + jwtUtil.issueAccessToken(userDetails.getUsername()));
                response.setHeader("refresh_token", "Bearer " + refreshToken);

                UsernamePasswordAuthenticationToken resultToken = new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(resultToken);
            } else {
                throw new AuthenticationException("Token is not valid");
            }
        } else {
            log.info("no valid JWT token found. uri = {}", request.getRequestURI());
        }
        chain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String refreshHeader = request.getHeader("refresh_token");
        return refreshHeader.substring("Bearer ".length());
    }
}
