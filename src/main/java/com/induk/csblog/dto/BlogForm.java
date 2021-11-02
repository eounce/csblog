package com.induk.csblog.dto;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Category;
import com.induk.csblog.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BlogForm {
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max=1000, message = "1000자 제한입니다.")
    private String content;

    private MultipartFile image;
    private Category category;
    private Member member;
    private String imageName;

    public static BlogForm blogToBlogFrom(Blog blog){
        BlogForm blogForm = new BlogForm();

        blogForm.setId(blog.getId());
        blogForm.setContent(blog.getContent());
        blogForm.setCategory(blog.getCategory());
        blogForm.setMember(blog.getMember());
        blogForm.setImageName(blog.getImage());
        blogForm.setTitle(blog.getTitle());
        return blogForm;
    }
}
