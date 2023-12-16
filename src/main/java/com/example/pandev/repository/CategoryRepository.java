package com.example.pandev.repository;

import com.example.pandev.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIdIsNull();

    List<Category> findByParentId(Category parent);

    Category findByName(String name);

    Category findByNameAndParentId(String name, Category parent);

}
