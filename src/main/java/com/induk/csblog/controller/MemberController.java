package com.induk.csblog.controller;

import com.induk.csblog.domain.Member;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.JoinForm;
import com.induk.csblog.dto.LoginForm;
import com.induk.csblog.service.MemberService;
import com.induk.csblog.util.FileStore;
import com.induk.csblog.util.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/csblog/members")
public class MemberController {

    private final FileStore fileStore;
    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("joinForm", new JoinForm());
        return "blog/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm,
                       BindingResult bindingResult) throws IOException {
        if(joinForm.getProfile().isEmpty())
            bindingResult.rejectValue("profile", "profileEmpty", "사진을 등록해 주세요");
        if(bindingResult.hasErrors()) {
            return "blog/join";
        }

        UploadFile uploadFile = fileStore.storeFile(joinForm.getProfile());
        Long id = memberService.add(joinForm, uploadFile);
        log.info("saved member id = {}", id);

        return "redirect:/csblog/members/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "blog/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "blog/login";
        }

        // 회원 정보 확인
        Member loginMember = memberService.login(loginForm);
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "blog/login";
        }

        // 로그인 성공
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/csblog";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) session.invalidate();

        return "redirect:/csblog";
    }
}
