package com.miniproject.config.security.formLogin;

import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.user.domain.UserStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class FormLoginProvider implements AuthenticationProvider {

    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    public FormLoginProvider(LoginService loginService, PasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
          throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetailsImpl userDetails = (UserDetailsImpl) loginService.loadUserByUsername(username);

        if (userDetails.getUser().getUserStatus() != UserStatus.VALID) {
            throw new AccountExpiredException("이메일 인증을 받지 않았습니다.");
        }

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
