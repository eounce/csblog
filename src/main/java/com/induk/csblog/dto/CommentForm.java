package com.induk.csblog.dto;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Comment;
import com.induk.csblog.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentForm {
    private Long id;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max=200, message = "200자 제한입니다.")
    private String content;
    private Member member;
    private Blog blog;

    public static CommentForm commentToCommentFrom(Comment comment){
        CommentForm commentForm = new CommentForm();

        commentForm.setId(comment.getId());
        commentForm.setContent(comment.getContent());
        commentForm.setMember(comment.getMember());
        commentForm.setBlog(comment.getBlog());
        return commentForm;
    }
}
