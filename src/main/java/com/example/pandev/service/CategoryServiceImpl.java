package com.example.pandev.service;

import com.example.pandev.entity.Category;
import com.example.pandev.model.CategoryModel;
import com.example.pandev.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;



    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public String getCategoriesTree() {
        List<Category> rootCategories = categoryRepository.findByParentIdIsNull();
        StringBuilder treeBuilder = new StringBuilder();
        List<CategoryModel> result = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            buildCategoryTree(rootCategory, treeBuilder, 0);
            final var categoryModel1 = new CategoryModel(rootCategory.getName());
            result.add(categoryModel1);
            buildCategoryModel(rootCategory, categoryModel1);
        }
        System.out.println(result);
        return treeBuilder.toString();
    }

    public List<CategoryModel> buildCategoriesModelTree() {
        List<Category> rootCategories = categoryRepository.findByParentIdIsNull();
        List<CategoryModel> result = new ArrayList<>();
        for (Category rootCategory : rootCategories) {
            final var categoryModel1 = new CategoryModel(rootCategory.getName());
            result.add(categoryModel1);
            buildCategoryModel(rootCategory, categoryModel1);
        }
        return result;
    }

    private void buildCategoryModel(Category category, CategoryModel categoryModel) {
        List<Category> childCategories = categoryRepository.findByParentId(category);
        for (Category childCategory : childCategories) {
            final var categoryModel1 = new CategoryModel(childCategory.getName());
            categoryModel.children.add(categoryModel1);
            buildCategoryModel(childCategory, categoryModel1);
        }
    }

    private void buildCategoryTree(Category category, StringBuilder treeBuilder, int depth) {
        for (int i = 0; i < depth; i++) {
            treeBuilder.append("  ");
        }
        treeBuilder.append("- ").append(category.getName()).append("\n");

        List<Category> childCategories = categoryRepository.findByParentId(category);
        for (Category childCategory : childCategories) {
            buildCategoryTree(childCategory, treeBuilder, depth + 1);
        }
    }

    @Override
    public void addElement(String elementName, Category parent){
        Category category = new Category();
        category.setName(elementName);
        category.setParentId(parent);
        categoryRepository.save(category);
    }

    @Override
    public void deleteElement(String elementName, Category parent) {
        Category categoryToDelete = categoryRepository.findByNameAndParentId(elementName, parent);

        if (categoryToDelete != null) {
            categoryRepository.delete(categoryToDelete);
        } else {
            throw new EntityNotFoundException("Категория не найдена для удаления: " + elementName);
        }
    }


    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }



}