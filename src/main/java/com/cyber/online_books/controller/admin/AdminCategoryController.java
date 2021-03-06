package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Category;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.category.CategoryNotFoundException;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "quan-tri/the_loai")
public class AdminCategoryController extends ExceptionHandling {
    private final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);

    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping ("/danh-sach")
    public ResponseEntity< ? > getAllListCategories(@RequestParam(name="keyword") String keyword,
                                            @RequestParam(name="pagenumber") Integer pagenumber, Principal principal) throws Exception {

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        return new ResponseEntity<>(categoryService.findCategoryBySearch(keyword
                , pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    @GetMapping ("/danh-sach-khong-phan-trang")
    public ResponseEntity< ? > getAllListCategoriesNoPagination(Principal principal) throws Exception {

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        return new ResponseEntity<>(categoryService.getListCategory(), HttpStatus.OK);
    }

    @PostMapping("/them")
    public ResponseEntity<Category> addCategory(@RequestBody Category category, Principal principal) throws UserNotLoginException, HttpMyException, UserNotFoundException {
        categoryService.checkUnique(null, category.getName());

        Category newCategory = new Category();
        if(StringUtils.isNotEmpty(category.getName())){
            newCategory.setName(category.getName());
            newCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        } else {
            throw new HttpMyException("T??n Th??? Lo???i Kh??ng ???????c ????? tr???ng");
        }

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        newCategory.setCreateBy(currentUsername);
        return new ResponseEntity<>(categoryService.save(newCategory), HttpStatus.OK);
    }

    @GetMapping("/cap-nhat/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Integer id, Principal principal) throws CategoryNotFoundException, UserNotLoginException, UserNotFoundException, HttpMyException {
        Category updateCategory = categoryService.findCategoryById(id);
        if(updateCategory == null)
            throw new CategoryNotFoundException("kh??ng t??m th???y th??? lo???i");
        if (principal == null) {
            throw new UserNotLoginException();
        }
        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        return new ResponseEntity<>(updateCategory, HttpStatus.OK);
    }

    @PostMapping("/cap-nhat/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable("id") Integer id, Principal principal) throws CategoryNotFoundException, UserNotLoginException, HttpMyException, UserNotFoundException {
        Category updateCategory = categoryService.findCategoryById(id);
        if(updateCategory == null)
            throw new CategoryNotFoundException("kh??ng t??m th???y th??? lo???i");
        categoryService.checkUnique(id, category.getName());
        if (principal == null) {
            throw new UserNotLoginException();
        }
        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        updateCategory.setName(category.getName());
        updateCategory.setStatus(category.getStatus());
        updateCategory.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        return new ResponseEntity<>(categoryService.save(updateCategory), HttpStatus.OK);
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("id") Integer id, Principal principal) throws CategoryNotFoundException, HttpMyException, UserNotLoginException, UserNotFoundException {
        Category category = categoryService.findCategoryById(id);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        if(category == null)
            throw new CategoryNotFoundException("kh??ng t??m th???y th??? lo???i");
        boolean result = categoryService.deleteCategory(id);
        if(result)
            return response(HttpStatus.OK, "th??? lo???i ???? x??a th??nh c??ng");
        else
            throw new HttpMyException("kh??ng th??? x??a th??? lo???i n??y");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
