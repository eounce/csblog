package com.induk.csblog.repository;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Category;
import com.induk.csblog.domain.Comment;
import com.induk.csblog.dto.CommentForm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAll();

    Optional<Comment> findById(Long commentId);

    Long countByBlogId(Long blogId);
}
