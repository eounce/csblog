package com.induk.csblog.controller;

import com.induk.csblog.domain.Category;
import com.induk.csblog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/csblog/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categoryList")
    @ResponseBody
    public List<Category> categoryList(){
        List<Category> categoryList = categoryService.categoryList();
        return categoryList;
    }
}
