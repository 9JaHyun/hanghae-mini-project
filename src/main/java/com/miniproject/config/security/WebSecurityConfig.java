package com.miniproject.config.security;

import com.miniproject.config.security.formLogin.FormLoginFilter;
import com.miniproject.config.security.formLogin.FormLoginProvider;
import com.miniproject.config.security.formLogin.LoginService;
import com.miniproject.config.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
              .ignoring()
              .requestMatchers(PathRequest.toStaticResources().atCommonLocations(),
                    PathRequest.toH2Console());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formLoginProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager(), jwtUtil);
        JwtCheckFilter jwtCheckFilter = new JwtCheckFilter(authenticationManager(), loginService, jwtUtil);
        http.csrf().disable();
        http
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
              .authorizeRequests(request ->
                    request
                          .antMatchers("/user/login", "/user/signup", "/").anonymous()
                          .anyRequest().authenticated())
              .addFilterAt(formLoginFilter, UsernamePasswordAuthenticationFilter.class)
              .addFilterAt(jwtCheckFilter, BasicAuthenticationFilter.class);
    }

    @Bean
    public FormLoginProvider formLoginProvider() {
        return new FormLoginProvider(loginService, passwordEncoder());
    }
}
