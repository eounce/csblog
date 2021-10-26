package com.induk.csblog.service;

import com.induk.csblog.domain.Member;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.JoinForm;
import com.induk.csblog.dto.LoginForm;
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

    public Member login(LoginForm loginForm) {
        return memberRepository.findByUid(loginForm.getUid())
                .filter(m -> m.getPw().equals(loginForm.getPw()))
                .orElse(null);
    }
}
