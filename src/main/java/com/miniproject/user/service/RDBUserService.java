package com.miniproject.user.service;

import com.miniproject.email.EmailUtil;
import com.miniproject.email.SignUpEmail;
import com.miniproject.user.domain.EmailAuth;
import com.miniproject.user.domain.User;
import com.miniproject.user.domain.UserStatus;
import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.repository.EmailAuthRepository;
import com.miniproject.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RDBUserService implements UserService {

    private final EmailAuthRepository emailAuthRepository;
    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;
    @Value("${email.expired-time}")
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");


    public RDBUserService(EmailAuthRepository emailAuthRepository, UserRepository userRepository,
          EmailUtil emailUtil, PasswordEncoder passwordEncoder) {
        this.emailAuthRepository = emailAuthRepository;
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {
        validateUsername(dto.getUsername());
        validatePassword(dto);
        validateDuplicateNickname(dto.getNickname());
        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserInfoDto sendUserInfo(String username) {
        return userRepository.findByUsername(username)
              .map(UserInfoDto::new)
              .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));
    }

    @Override
    public boolean checkValidUser(String username) {
        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("잘못된 로그인 정보입니다."));

        return user.getUserStatus() == UserStatus.VALID;
    }

    @Override
    public void createCertificationCode(String email, String url) {
        String certificationCode = UUID.randomUUID().toString();
        log.info(url);
        emailUtil.sendEmail(email, new SignUpEmail(certificationCode, url, email, certificationCode));

        EmailAuth emailAuth = new EmailAuth(email, certificationCode);
        emailAuthRepository.save(emailAuth);
    }

    @Override
    @Transactional
    public void verityEmail(String email, String authKey) {
        EmailAuth emailAuth = emailAuthRepository.findByUsername(email)
              .orElseThrow(() -> new IllegalArgumentException("만료되었거나 유효하지 않는 인증 링크입니다."));

        if (emailAuth.getAuthKey().equals(authKey)) {
            User user = userRepository.findByUsername(email)
                  .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

            log.info("user valid! -> {}", user.getUsername());
            user.changeStatus(UserStatus.VALID);
            emailAuthRepository.delete(emailAuth);
        } else {
            throw new IllegalArgumentException("만료되었거나 유효하지 않는 인증 링크입니다.");
        }
    }

    @Override
    public void validateUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
        }
    }

    @Override
    public void validateDuplicateNickname(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            throw new IllegalArgumentException("중복되는 닉네임입니다.");
        }
    }

    private void validatePassword(SignUpRequestDto dto) {
        if (!PASSWORD_PATTERN.matcher(dto.getPassword()).matches()) {
            throw new IllegalArgumentException(("비밀번호는 최소 8자리에 숫자, 문자, 특수문자를 각각 1개 이상 포함해야 합니다."));
        }
        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 재확인이 일치하지 않습니다.");
        }
    }
}
