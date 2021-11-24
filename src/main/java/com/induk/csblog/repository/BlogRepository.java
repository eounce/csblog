package com.induk.csblog.repository;

import com.induk.csblog.domain.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("select c from Blog c" +
            " join fetch c.member" +
            " order by c.createDate desc")
    List<Blog> findTop5ByOrderByCreateDateDesc(Pageable pageable);

    @Query("select c from Blog c" +
            " join fetch c.member" +
            " join fetch c.category g" +
            " where c.title like CONCAT('%',:searchText, '%')" +
            " and g.id = :categoryId" +
            " order by c.createDate desc")
    List<Blog> findAllByCategoryIdAndTitleContainingOrderByCreateDateDesc(Long categoryId, String searchText, Pageable pageable);
    List<Blog> findAllByTitleContainingOrderByCreateDateDesc(String searchText,Pageable pageable);

    @Query("select c from Blog c" +
        " join fetch c.member" +
        " left join fetch c.comments cs" +
        " left join fetch cs.member" +
        " where c.id = :blogId")
    Optional<Blog> findById(Long blogId);

    Long countByCategoryIdAndTitleContaining(Long categoryId, String searchText);
    Long countByTitleContaining(String searchText);
}
