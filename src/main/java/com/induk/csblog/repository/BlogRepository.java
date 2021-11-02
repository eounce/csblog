package com.induk.csblog.repository;

import com.induk.csblog.domain.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findTop5ByOrderByCreateDateDesc();
    List<Blog> findAllByCategoryIdAndTitleContainingOrderByCreateDateDesc(Long categoryId, String searchText,Pageable pageable);
    Optional<Blog> findById(Long blogId);
    //@Query(value = "select DISTINCT b from Blog b left join fetch b.category where b.id = :blogId")
    //Optional<Blog> findById(@Param("blogId") Long blogId);

    Long countByCategoryIdAndTitleContaining(Long categoryId, String searchText);
}
