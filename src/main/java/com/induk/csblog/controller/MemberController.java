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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;

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

        UploadFile uploadFile = fileStore.storeFile(joinForm.getProfile(), "member");
        Long id = memberService.add(joinForm, uploadFile);
        log.info("saved member id = {}", id);

        return "redirect:/csblog/members/login";
    }

    @GetMapping("/{memberId}/update")
    public String updateForm(@PathVariable(name = "memberId") Long id, Model model) {
        Member findMember = memberService.getMemberById(id);
        JoinForm joinForm = new JoinForm();
        joinForm.setId(id);
        joinForm.setName(findMember.getName());
        joinForm.setPw(findMember.getPw());
        joinForm.setStudentId(findMember.getStudentId());
        joinForm.setUid(findMember.getUid());
        joinForm.setTel(findMember.getTel());
        joinForm.setFileName(findMember.getProfile());

        model.addAttribute("joinForm", joinForm);
        return "blog/update";
    }

    @PostMapping("/{memberId}/update")
    public String updateMember(@PathVariable(name = "memberId") Long id,
                               @Valid JoinForm joinForm,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()) {
            return "blog/update";
        }

        UploadFile uploadFile;
        if(joinForm.getProfile().isEmpty()) {
            uploadFile = new UploadFile("", joinForm.getFileName());
        } else {
            uploadFile = fileStore.storeFile(joinForm.getProfile(), "member");
        }

        memberService.update(joinForm, id, uploadFile);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/csblog/members/{id}/update";
    }

    @GetMapping("/{memberId}/delete")
    public String deleteMember(@PathVariable(name = "memberId") Long id, HttpServletRequest request) throws IOException {
        memberService.delete(id);

        HttpSession session = request.getSession(false);
        if(session != null) session.invalidate();

        return "redirect:/csblog";
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

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename, "member"));
    }
}
