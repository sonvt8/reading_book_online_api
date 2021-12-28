package com.cyber.online_books.controller.home;

import com.cyber.online_books.response.CategoryResponse;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/home/category")
public class CategoryController {

    private final CategoryService categoryService;
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCategories(){
        List<CategoryResponse> listCategories = categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED);

        return ResponseEntity.status(HttpStatus.OK).body(listCategories);
    }
}
