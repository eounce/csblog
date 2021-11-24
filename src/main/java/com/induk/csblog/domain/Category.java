package com.induk.csblog.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.induk.csblog.dto.CategoryForm;
import com.induk.csblog.service.CategoryService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private Category superCategory;



    @OneToMany(fetch = FetchType.LAZY,mappedBy = "superCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    //@Transient
    private List<Category> subCategory;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    //@Transient
    private List<Blog> blog;


    public static Category createCategory(String name, Category superCategory) {
        Category category = new Category();
        category.name = name;
        category.superCategory = superCategory;
        return category;
    }

    public static Category toCategory(Long id, String name, Category superCategory){
        Category category = new Category();
        category.id = id;
        category.name = name;
        category.superCategory =superCategory;
        return category;
    }
    public CategoryForm toForm(){
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setId(id);
        categoryForm.setCategoryId(((superCategory != null)? superCategory.id : null));
        categoryForm.setName(name);
        return  categoryForm;
    }

    public void update(String name, Category superCategory){
        this.name = name;
        this.superCategory = superCategory;
    }
    public void setBlog(List<Blog> blogList){
        this.blog = blogList;
    }
}
