package com.induk.csblog.repository;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);
    Optional<Member> findById(Long memberId);


}
