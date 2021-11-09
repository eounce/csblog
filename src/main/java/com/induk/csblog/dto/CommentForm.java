package com.induk.csblog.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Comment;
import com.induk.csblog.domain.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CommentForm {
    private Long id;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max=200, message = "200자 제한입니다.")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime createDate;

    private Member member;
    private Blog blog;

    public static CommentForm commentToCommentForm(Comment comment){
        CommentForm commentForm = new CommentForm();

        commentForm.setId(comment.getId());
        commentForm.setContent(comment.getContent());
        commentForm.setCreateDate(comment.getCreateDate());
        commentForm.setMember(comment.getMember());
        commentForm.setBlog(comment.getBlog());

        return commentForm;
    }
}
