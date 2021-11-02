package com.induk.csblog.aop;


import com.induk.csblog.domain.Member;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@Component
@Aspect
public class LoginAspect {

    @Pointcut("execution(* com.induk.csblog.controller.BlogController.addForm(..)) || execution(* com.induk.csblog.controller.BlogController.editForm(..))")
    public void loginCheckPointcut() {
    }

    @Before("loginCheckPointcut()")
    public void loginCheck() throws IOException {
        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        Member m = (Member)session.getAttribute("loginMember");

        if(m == null || m.getId() == null){
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");


            PrintWriter writer = response.getWriter();
            writer.print("<script>");
            writer.print("alert('로그인이 필요한 서비스입니다');");
            writer.print("location.href='/csblog/members/login'");
            writer.print("</script>");
            writer.close();
        }
    }
}
