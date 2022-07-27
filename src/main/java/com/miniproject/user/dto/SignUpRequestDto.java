package com.miniproject.user.dto;

import com.miniproject.user.domain.User;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto implements Serializable {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String rePassword;

    @NotBlank
    private String nickname;

    private MultipartFile profile;

    public User toEntity() {
        return new User(username, password, nickname);
    }
}
