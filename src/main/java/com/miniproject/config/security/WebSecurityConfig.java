package com.miniproject.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.miniproject.config.security.exceptionHandler.JwtAccessDeniedHandler;
import com.miniproject.config.security.formLogin.FormLoginFilter;
import com.miniproject.config.security.formLogin.FormLoginProvider;
import com.miniproject.config.security.formLogin.LoginService;
import com.miniproject.config.security.handler.CustomLogoutSuccessHandler;
import com.miniproject.config.security.jwt.JWTUtil;
import com.miniproject.config.security.jwt.JwtCheckFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LoginService loginService;
    private final LogoutHandler logoutHandler;
    private final JWTUtil redisJwtUtil;


    // exceptionHandler
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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
        FormLoginFilter formLoginFilter = new FormLoginFilter(authenticationManager(), redisJwtUtil);
        JwtCheckFilter jwtCheckFilter = new JwtCheckFilter(authenticationManager(), loginService,
              redisTemplate, redisJwtUtil);
        http.csrf().disable();
        http.httpBasic().disable();
        http.cors(withDefaults());
        http
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
              .authorizeRequests(request ->
                    request
                          .antMatchers("/").permitAll()
                          .antMatchers("/user/login", "/user/signup", "/user/certification")
                          .anonymous()
                          .antMatchers(HttpMethod.GET, "/posts").permitAll()
                          .antMatchers(HttpMethod.GET, "/posts/**").permitAll()
                          .antMatchers(HttpMethod.GET, "/posts/**/comments").permitAll()
                          .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                          .anyRequest().authenticated())
              .addFilterAt(formLoginFilter, UsernamePasswordAuthenticationFilter.class)
              .addFilterAt(jwtCheckFilter, BasicAuthenticationFilter.class);
        http
              .logout()
              .addLogoutHandler(logoutHandler)
              .logoutSuccessHandler(logoutSuccessHandler());
        http.exceptionHandling()
              .authenticationEntryPoint(customAuthenticationEntryPoint)
              .accessDeniedHandler(jwtAccessDeniedHandler);
    }

    @Bean
    public FormLoginProvider formLoginProvider() {
        return new FormLoginProvider(loginService, passwordEncoder());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}
