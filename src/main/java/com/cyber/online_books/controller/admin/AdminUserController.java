package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Cyber_Group
 * @project book_online
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan_tri/nguoi_dung")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page< User > users = userService.getUsers(page, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(users, OK);
    }

    @PostMapping("/cap_nhat")
    public ResponseEntity<User> saveUpdateUser(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User editedUser = userService.updateUser(user);
        return new ResponseEntity<>(editedUser, OK);
    }

    @PostMapping("/nap_dau")
    public ResponseEntity<HttpResponse> submitPayDraw(@RequestParam("money") String money,
                                              @RequestParam("reId") Long reId, Principal principal) throws UserNotFoundException, HttpMyException {
        userService.topUp(Double.valueOf(money),reId, principal);
        return response(OK, "User đã tăng số đậu tương ứng");
    }

    @GetMapping("/xoa/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long userId, Principal principal) throws HttpMyException, IOException, UserNotFoundException {
        userService.deleteUser(principal, userId);
        return response(OK, "User đã được xoá");
    }

    @GetMapping(value = "/danh-sach")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam(value = "search", defaultValue = "") String search,
                                                    @RequestParam("type") Integer type,
                                                    @RequestParam("pagenumber") Integer pagenumer,
                                                    Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        return new ResponseEntity<>(userService.findByType(search, type, pagenumer, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
