package com.example.pandev.service;

import com.example.pandev.model.CategoryModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;


public interface DocumentService {


    XSSFWorkbook generateDocument();

    void write(CategoryModel category, int cellNumber, XSSFSheet xssfSheet);

    void write(String text, int cellNumber, XSSFSheet xssfSheet);


}
