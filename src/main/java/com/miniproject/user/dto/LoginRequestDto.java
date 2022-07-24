package com.miniproject.user.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginRequestDto implements Serializable {

    private String username;
    private String password;
}
