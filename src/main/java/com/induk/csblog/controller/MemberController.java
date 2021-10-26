package com.induk.csblog.controller;

import com.induk.csblog.domain.Member;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.JoinForm;
import com.induk.csblog.service.MemberService;
import com.induk.csblog.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
        if(bindingResult.hasErrors()) {
            return "blog/join";
        }

        UploadFile uploadFile = fileStore.storeFile(joinForm.getProfile());
        Long id = memberService.add(joinForm, uploadFile);
        log.info("saved member id = {}", id);

        return "redirect:/csblog";
    }
}
