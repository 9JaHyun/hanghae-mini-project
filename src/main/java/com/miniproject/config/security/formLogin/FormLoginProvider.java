package com.miniproject.config.security.formLogin;

import com.miniproject.user.domain.User;
import com.miniproject.user.domain.UserStatus;
import com.miniproject.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class FormLoginProvider implements AuthenticationProvider {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public FormLoginProvider(LoginService loginService, UserRepository userRepository,
          PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("잘못된 로그인 정보입니다."));

        if (user.getUserStatus() == UserStatus.NOT_VALID) {
            throw new IllegalStateException("인증이 완료되지 않은 계정입니다. 이메일 인증을 수행해주세요");
        }

        UserDetails userDetails = loginService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, null);
        } else {
            throw new BadCredentialsException("잘못된 로그인 정보입니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
