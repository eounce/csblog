package com.induk.csblog.controller;

import com.induk.csblog.service.BlogService;
import com.induk.csblog.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/csblog")
public class MainController {
    private final BlogService blogService;

    @GetMapping
    public String home(Model model, @ModelAttribute("searchText") String searchText,
                       @RequestParam(value = "page", defaultValue = "1") String page) {

        PaginationInfo paginationInfo = new PaginationInfo(Integer.parseInt(page), 4, 5);
        paginationInfo.setTotalRecordCount(blogService.searchByBlogTitleCount(searchText).intValue());

        model.addAttribute("lastBlogList", blogService.lastBlogList());
        model.addAttribute("blogList", blogService.searchByBlogTitle(searchText, paginationInfo));
        model.addAttribute("newLineChar", '\n');
        model.addAttribute("page", paginationInfo);
        return "blog/main";
    }
}
