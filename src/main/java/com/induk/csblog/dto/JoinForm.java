package com.induk.csblog.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {
    @Email(message = "메일 형식을 확인해주세요")
    @NotEmpty(message = "아이디를 입력해주세요")
    private String uid;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String pw;

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "전화번호를 입력해주세요")
    private String tel;

    @NotEmpty(message = "학번을 입력해주세요")
    private String studentId;

    private MultipartFile profile;
}
