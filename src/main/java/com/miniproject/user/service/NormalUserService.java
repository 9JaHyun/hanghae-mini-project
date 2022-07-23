package com.miniproject.user.service;

import com.miniproject.user.domain.User;
import com.miniproject.user.dto.SignUpRequestDto;
import com.miniproject.user.repository.UserRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NormalUserService implements UserService{

    private final UserRepository userRepository;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
          "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");

    public NormalUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void signUp(SignUpRequestDto dto) {
        validateUsername(dto);
        validatePassword(dto);
        validateDuplicateNickname(dto);

        userRepository.save(dto.toEntity());
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
