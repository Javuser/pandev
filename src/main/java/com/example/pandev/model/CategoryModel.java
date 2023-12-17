package com.example.pandev.model;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {

        public String name;
        public List<CategoryModel> children;

        public CategoryModel(String name) {
            this.name = name;
            this.children = new ArrayList<>();
        }

        @Override
        public String toString() {
            return "CategoryModel{" +
                    "name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
}
