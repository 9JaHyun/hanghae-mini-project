package com.miniproject.config.security.formLogin;

import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("잘못된 로그인 정보입니다."));

        return new UserDetailsImpl(user);
    }

}
