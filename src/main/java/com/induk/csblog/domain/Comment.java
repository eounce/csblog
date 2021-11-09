package com.induk.csblog.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024, nullable = false)
    private String content;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    @JsonManagedReference
    private Blog blog;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonManagedReference
    private Member member;

    @Builder
    public Comment(String content, LocalDateTime createDate, Blog blog, Member member) {
        this.content = content;
        this.createDate = createDate;
        this.blog = blog;
        this.member = member;
    }

    public static Comment createComment(String content, Member member, Blog blog){
        Comment comment = new Comment();
        comment.content = content;
        comment.member = member;
        comment.blog = blog;
        comment.createDate = LocalDateTime.now();
        return comment;
    }
}
