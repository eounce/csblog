package com.induk.csblog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 1024, nullable = false)
    private String content;
    private String image;
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    @JsonBackReference
    List<Comment> comments = new ArrayList<>();

    public static Blog createBlog(String title, String content, String image, Member member, Category category){
        Blog blog = new Blog();
        blog.title = title;
        blog.content = content;
        blog.image = image;
        blog.member = member;
        blog.category = category;
        blog.createDate = LocalDateTime.now();
        return blog;
    }
}
