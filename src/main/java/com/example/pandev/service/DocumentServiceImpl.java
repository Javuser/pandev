package com.example.pandev.service;

import com.example.pandev.entity.Category;
import com.example.pandev.model.CategoryModel;
import com.example.pandev.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService{

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;


    public DocumentServiceImpl(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    public XSSFWorkbook generateDocument() {
        List<CategoryModel> categoryModels = categoryService.buildCategoriesModelTree();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet 1");
        for (CategoryModel model : categoryModels) {
            write(model,0, sheet);
        }
//        String fileName = "/Users/nurbakyt/Desktop/" + Instant.now() + "_categories.xlsx";
//        try (FileOutputStream out = new FileOutputStream(fileName)) {
//            workbook.write(out);
//            log.info("document written successfully on disk. Filename: {}", fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error while generating file");
//        }

        return workbook;

    }

    public void write(CategoryModel category, int cellNumber, XSSFSheet xssfSheet) {
        write(category.name, cellNumber, xssfSheet);

        if (!category.children.isEmpty()) {
            cellNumber = cellNumber + 1;
        }
        for (CategoryModel childCategory: category.children) {
            write(childCategory, cellNumber, xssfSheet);
        }
    }

    public void write(String text, int cellNumber, XSSFSheet xssfSheet) {
        int lastRowNum = xssfSheet.getLastRowNum();
        int rowNumber = lastRowNum == -1 ? 0: lastRowNum + 1;
        Row row = xssfSheet.createRow(rowNumber);
        Cell cell = row.createCell(cellNumber);
        cell.setCellValue(text);
    }

}
