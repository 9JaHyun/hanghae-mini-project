package com.miniproject.user.service;

import com.miniproject.email.EmailUtil;
import com.miniproject.email.SignUpEmail;
import com.miniproject.user.domain.User;
import com.miniproject.user.dto.CertificationCodeDto;
import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.repository.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;
    private Map<String, String> codeStorage = new ConcurrentHashMap();
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");

    public UserService(UserRepository userRepository, EmailUtil emailUtil,
          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailUtil = emailUtil;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void signUp(SignUpRequestDto dto) {
        validateUsername(dto);
        validatePassword(dto);
        validateDuplicateNickname(dto);
        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    public void requestCertificationCode(String email) {
        String certificationCode = UUID.randomUUID().toString().substring(0, 8);
        codeStorage.put(email, certificationCode);
        emailUtil.sendEmail(email, new SignUpEmail(certificationCode));
    }

    public void checkCertificationCode(CertificationCodeDto dto) {
        if (!codeStorage.get(dto.getEmail()).equals(dto.getCode())) {
            codeStorage.remove(dto.getEmail());
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        codeStorage.remove(dto.getEmail());
    }

    private void validateUsername(SignUpRequestDto dto) {
        Optional<User> user = userRepository.findByUsername(dto.getUsername());
        if (user.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 계정입니다.");
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

    private void validateDuplicateNickname(SignUpRequestDto dto) {
        Optional<User> user = userRepository.findByNickname(dto.getNickname());
        if (user.isPresent()) {
            throw new IllegalArgumentException("중복되는 닉네임입니다.");
        }
    }
}
