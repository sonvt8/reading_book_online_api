package com.cyber.online_books.controller.admin;

import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/category/admin")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping ("/list")
    public ResponseEntity< ? > loadCategory(@RequestParam(name="keyword") String keyword,
                                            @RequestParam(name="pagenumber") Integer pagenumber) throws Exception {

        return new ResponseEntity<>(categoryService.findCategoryBySearch(keyword
                , pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
}
