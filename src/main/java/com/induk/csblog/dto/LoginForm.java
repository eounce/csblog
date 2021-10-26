package com.induk.csblog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @Email(message = "이메일 형식을 확인해주세요")
    @NotEmpty(message = "아이디를 입력해주세요")
    private String uid;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String pw;
}
