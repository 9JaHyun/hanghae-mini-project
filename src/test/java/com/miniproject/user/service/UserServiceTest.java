package com.miniproject.user.service;

import com.miniproject.user.dto.SignUpRequestDto;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class UserServiceTest {

    private static Validator validator;

    @Autowired
    private RedisUserService userService;

    @DisplayName(value = "회원 아이디 중복이 되어서는 안된다.")
    @Test
    void usernameDuplicationTest() {
        SignUpRequestDto dto1 = new SignUpRequestDto("username@gmail.com", "password1!",
              "password1!", "user1", null);
        SignUpRequestDto dto2 = new SignUpRequestDto("username@gmail.com", "password1!",
              "password1!", "user2", null);

        Assertions.assertThatNoException().isThrownBy(() -> userService.signUp(dto1));
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> userService.signUp(dto2))
              .withMessageContaining("이미 존재하는 계정");
    }

    @DisplayName(value = "아이디가 이메일 형식을 지키는가?.")
    @Test
    void isUsernameEmailFormTest() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<SignUpRequestDto>> result1 = validator.validate(
              new SignUpRequestDto("username1@gmail.com", "password1!", "password1!", "user1",
                    null));
        Set<ConstraintViolation<SignUpRequestDto>> result2 = validator.validate(
              new SignUpRequestDto("username2", "password1!", "password1!", "user2", null));

        Assertions.assertThat(result1).isEmpty();
        Assertions.assertThat(result2).isNotEmpty();
    }

    @DisplayName(value = "비밀번호와 비밀번호 재입력은 일치해야 한다.")
    @Test
    void passwordEqualsTest() {
        SignUpRequestDto dto1 = new SignUpRequestDto("username@gmail.com", "password1!",
              "password1!",
              "user1", null);
        SignUpRequestDto dto2 = new SignUpRequestDto("username1@gmail.com", "password2!",
              "password1!",
              "user2", null);

        Assertions.assertThatCode(() -> userService.signUp(dto1)).doesNotThrowAnyException();
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> userService.signUp(dto2))
              .withMessageContaining("비밀번호 재확인");
    }

    @DisplayName(value = "비밀번호는 최소 8자리에 숫자, 문자, 특수문자를 각각 1개 이상 포함해야 한다")
    @Test
    void passwordRegexTest() {
        SignUpRequestDto dto1 = new SignUpRequestDto("username1@gmail.com", "password1!",
              "password1!", "user1", null);
        SignUpRequestDto dto2 = new SignUpRequestDto("username2@gmail.com", "pw", "w",
              "user2", null);

        Assertions.assertThatNoException().isThrownBy(() -> userService.signUp(dto1));
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> userService.signUp(dto2))
              .withMessageContaining("8자리에 숫자, 문자, 특수문자");
    }

    @DisplayName(value = "닉네임은 중복되어서는 안된다.")
    @Test
    void duplicateNicknameTest() {
        SignUpRequestDto dto1 = new SignUpRequestDto("username1", "password1!", "password1!",
              "user1", null);
        SignUpRequestDto dto2 = new SignUpRequestDto("username2", "password1!", "password1!",
              "user1", null);

        Assertions.assertThatNoException().isThrownBy(() -> userService.signUp(dto1));
        Assertions.assertThatIllegalArgumentException().isThrownBy(() -> userService.signUp(dto2))
              .withMessageContaining("닉네임");

    }
}