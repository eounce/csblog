package com.induk.csblog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String uid;
    private String pw;
    private String name;
    private String tel;
    private String studentId;
    private String profile;
    private LocalDate createDate;

    //== 생성 메서드 ==//
    public static Member createMember(String uid, String pw,
                                      String name, String tel,
                                      String studentId, String profile) {
        Member member = new Member();
        member.uid = uid;
        member.pw = pw;
        member.name = name;
        member.tel = tel;
        member.studentId = studentId;
        member.profile = profile;
        member.createDate = LocalDate.now();
        return member;
    }

    //== 비즈니스 로직 ==//
    public void changeMember(String pw, String tel, String profile) {
        this.pw = pw;
        this.tel = tel;
        this.profile = profile;
    }
}
