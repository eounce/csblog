package com.induk.csblog.service;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Category;
import com.induk.csblog.dto.CategoryForm;
import com.induk.csblog.repository.BlogRepository;
import com.induk.csblog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BlogRepository blogRepository;

    public List<Category> categoryList(){
        return categoryRepository.findAll();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Long add(CategoryForm categoryForm){
        String name = categoryForm.getName();
        Category superCategory = findById(categoryForm.getCategoryId());
        Category category = categoryRepository.save(Category.toCategory(null, name, superCategory));
        return category.getId();
    }
    @Transactional
    public void update(CategoryForm categoryForm){
        Category category = findById(categoryForm.getId());
        if(category != null){
            String name = categoryForm.getName();
            Category superCategory = findById(categoryForm.getCategoryId());
            category.update(name, superCategory);
        }
    }
    @Transactional
    public void del(Long categoryId){
        Optional<Category> category = categoryRepository.findById(categoryId);

        category.ifPresent(delCategory ->{
            categoryRepository.delete(delCategory);
        });
    }
}
