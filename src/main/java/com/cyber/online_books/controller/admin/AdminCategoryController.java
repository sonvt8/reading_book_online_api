package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.domain.UserPrincipal;
import com.cyber.online_books.entity.Category;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.category.CategoryNotFoundException;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PrePersist;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/admin/category")
public class AdminCategoryController extends ExceptionHandling {

    @Autowired
    private CategoryService categoryService;

    @GetMapping ("/list")
    public ResponseEntity< ? > getAllListCategories(@RequestParam(name="keyword") String keyword,
                                            @RequestParam(name="pagenumber") Integer pagenumber) throws Exception {

        return new ResponseEntity<>(categoryService.findCategoryBySearch(keyword
                , pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String username="";
        if (principal instanceof UserPrincipal) {
            username = ((UserPrincipal) principal).getUsername();
        }
        newCategory.setCreateBy("administrator");
        return new ResponseEntity<>(categoryService.save(newCategory), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable("id") Integer id) throws CategoryNotFoundException {
        Category updateCategory = categoryService.findCategoryById(id);
        if(updateCategory == null)
            throw new CategoryNotFoundException("Not found Category");
        updateCategory.setName(category.getName());
        updateCategory.setStatus(category.getStatus());
        updateCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        return new ResponseEntity<>(categoryService.save(updateCategory), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("id") Integer id) throws CategoryNotFoundException {
        categoryService.deleteCategory(id);
        return response(HttpStatus.OK, "Category deleted successfully");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
