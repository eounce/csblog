package com.induk.csblog.controller;

import com.induk.csblog.domain.Category;
import com.induk.csblog.dto.CategoryForm;
import com.induk.csblog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/csblog/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categoryList")
    @ResponseBody
    public List<Category> categoryList(){

        List<Category> categoryList = categoryService.categoryList();
        //String n = (categoryList.get(1).getBlog() == null)? "null" : categoryList.get(1).getBlog().get(0).getTitle();
        //log.info("\n\n\n\n\ncategoryName: " + categoryList.get(1).getName());
        //log.info("\n\n\n\n\nblogName: " + n);
        return categoryList;
    }

    @GetMapping
    public String listForm(Model model) {
        model.addAttribute("categoryList", categoryService.categoryList());
        return "blog/category/listForm";
    }
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("categoryList", categoryService.categoryList());
        model.addAttribute("categoryForm", new CategoryForm());
        return "blog/category/addForm";
    }
    @PostMapping("/add")
    public String addForm(Model model, @Valid CategoryForm categoryForm,
                          BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            model.addAttribute("categoryList", categoryService.categoryList());
            model.addAttribute("categoryForm", new CategoryForm());
            return "blog/category/addForm";
        }
        categoryService.add(categoryForm);

        return "redirect:/csblog/category";
    }
    @GetMapping("/edit/{categoryId}")
    public String editForm(Model model, @PathVariable Long categoryId) {
        List<Category> categoryList = categoryService.categoryList();
        CategoryForm categoryForm = categoryService.findById(categoryId).toForm();
        boolean superYN = false;
        for(Category c : categoryList){
            if(c.getSuperCategory() !=null && categoryForm.getId() == c.getSuperCategory().getId())superYN = true;
        }

        model.addAttribute("superYN", superYN);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryForm", categoryForm);
        return "blog/category/editForm";
    }
    @PostMapping("/edit/{categoryId}")
    public String editForm(Model model, @PathVariable Long categoryId,
                           @Valid CategoryForm categoryForm, BindingResult bindingResult) {
        categoryForm.setId(categoryId);
        if(bindingResult.hasErrors()) {
            List<Category> categoryList = categoryService.categoryList();
            boolean superYN = false;
            for(Category c : categoryList){
                if(c.getSuperCategory() !=null && categoryForm.getId() == c.getSuperCategory().getId())superYN = true;
            }
            model.addAttribute("superYN", superYN);
            model.addAttribute("categoryList", categoryList);
            model.addAttribute("categoryForm", categoryForm);
            return "blog/category/editForm";
        }
        categoryService.update(categoryForm);

        return "redirect:/csblog/category";
    }
    @RequestMapping(value = "/del/{categoryId}", method = {RequestMethod.POST})
    public String delForm(@PathVariable Long categoryId){
        categoryService.del(categoryId);
        return "redirect:/csblog/category";
    }
}
