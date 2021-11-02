package com.induk.csblog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @ColumnDefault("0")
    private Long categoryId;

    public static Category createCategory(String name, Long categoryId) {
        Category category = new Category();
        category.name = name;
        category.categoryId = categoryId;
        return category;
    }
}
