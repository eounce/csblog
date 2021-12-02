package com.induk.csblog.service;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Member;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.JoinForm;
import com.induk.csblog.dto.LoginForm;
import com.induk.csblog.repository.BlogRepository;
import com.induk.csblog.repository.CommentRepository;
import com.induk.csblog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long add(JoinForm joinForm, UploadFile uploadFile) {
        Member member = Member.createMember(joinForm.getUid(),
                joinForm.getPw(),
                joinForm.getName(),
                joinForm.getTel(),
                joinForm.getStudentId(),
                uploadFile.getStoreFileName());

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Transactional
    public void update(JoinForm joinForm, Long id, UploadFile uploadFile) {
        Member member = memberRepository.findById(id).orElse(null);

        if(member != null) {
            member.changeMember(joinForm.getPw(), joinForm.getTel(), uploadFile.getStoreFileName());
        }
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteAllByMemberId(id);
        blogRepository.deleteAllByMemberId(id);

        Member member = memberRepository.findById(id).orElse(null);
        memberRepository.delete(member);
    }

    public Member login(LoginForm loginForm) {
        return memberRepository.findByUid(loginForm.getUid())
                .filter(m -> m.getPw().equals(loginForm.getPw()))
                .orElse(null);
    }

    public Member getMemberById(Long memberId){

        return memberRepository.findById(memberId).orElse(null);
    }
}
