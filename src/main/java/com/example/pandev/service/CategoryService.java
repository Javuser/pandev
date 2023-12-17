package com.example.pandev.service;

import com.example.pandev.entity.Category;
import com.example.pandev.model.CategoryModel;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();


    String getCategoriesTree();

    void addElement(String elementName, Category parent);

    void deleteElement(String elementName, Category parent);

    Category getCategoryByName(String categoryName);
    List<CategoryModel> buildCategoriesModelTree();
}
