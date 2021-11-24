package com.induk.csblog.dto;

import jdk.jfr.Category;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class CategoryForm {
    private  Long id;
    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    private Long categoryId;

}
