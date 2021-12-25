package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.domain.UserPrincipal;
import com.cyber.online_books.entity.Category;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.category.CategoryNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
    private final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping ("/list")
    public ResponseEntity< ? > getAllListCategories(@RequestParam(name="keyword") String keyword,
                                            @RequestParam(name="pagenumber") Integer pagenumber) throws Exception {

        return new ResponseEntity<>(categoryService.findCategoryBySearch(keyword
                , pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category, Principal principal) throws UserNotLoginException {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        newCategory.setCreateBy(currentUsername);
        return new ResponseEntity<>(categoryService.save(newCategory), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable("id") Integer id, Principal principal) throws CategoryNotFoundException, UserNotLoginException {
        Category updateCategory = categoryService.findCategoryById(id);
        if(updateCategory == null)
            throw new CategoryNotFoundException("Not found Category for update");
        if (principal == null) {
            throw new UserNotLoginException();
        }
        updateCategory.setName(category.getName());
        updateCategory.setStatus(category.getStatus());
        updateCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        return new ResponseEntity<>(categoryService.save(updateCategory), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("id") Integer id, Principal principal) throws CategoryNotFoundException, HttpMyException, UserNotLoginException {
        Category category = categoryService.findCategoryById(id);
        if (principal == null) {
            throw new UserNotLoginException();
        }
        if(category == null)
            throw new CategoryNotFoundException("Not found Category for delete");
        boolean result = categoryService.deleteCategory(id);
        if(result)
            return response(HttpStatus.OK, "Category deleted successfully");
        else
            throw new HttpMyException("Can not delete this category");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
