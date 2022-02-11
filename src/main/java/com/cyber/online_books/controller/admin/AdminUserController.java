package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Role;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.repository.RoleRepository;
import com.cyber.online_books.service.RoleService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page< User > users = userService.getUsers(page, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(users, OK);
    }

    @GetMapping("/phan_quyen")
    public ResponseEntity<List<Role>> getlistRoles() {
        List<Role> listRoles = roleService.getAllRole();
        return new ResponseEntity<>(listRoles, OK);
    }

    @PostMapping("/cap_nhat")
    public ResponseEntity<User> saveUpdateUser(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User editedUser = userService.updateUser(user);
        return new ResponseEntity<>(editedUser, OK);
    }

//    @PostMapping("/cap_nhat/{id}")
//    public ResponseEntity<User> saveUpdateAdminUser(@RequestParam("role") Set<String> role,
//                                                    @RequestParam("status") Integer status,
//                                                    @PathVariable("id") Long id) throws UserNotFoundException, UsernameExistException, EmailExistException {
//
//        User editedUser = userService.findUserById(id);
//        editedUser.setRoleList(Arrays.stream(role.toArray(new String[0])).map(r -> roleRepository.findByName(r)).collect(Collectors.toSet()));
//        editedUser.setStatus(status);
//
//        return new ResponseEntity<>(userService.save(editedUser), OK);
//    }

    @PostMapping("/cap_nhat/{id}")
    public ResponseEntity<User> saveUpdateAdminUser(@RequestBody User user,
                                                    @PathVariable("id") Long id) throws UserNotFoundException, UsernameExistException, EmailExistException {

        User editedUser = userService.findUserById(id);
        editedUser.setRoleList(user.getRoleList());
        editedUser.setStatus(user.getStatus());

        return new ResponseEntity<>(userService.save(editedUser), OK);
    }

    @GetMapping("/cap_nhat/{id}")
    public ResponseEntity<User> getAdminUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/nap_dau")
    public ResponseEntity<HttpResponse> submitPayDraw(@RequestParam("money") String money,
                                              @RequestParam("reId") Long reId, Principal principal) throws UserNotFoundException, HttpMyException, UserNotLoginException {
        userService.topUp(Double.valueOf(money),reId, principal);
        return response(OK, "User đã tăng số đậu tương ứng");
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long userId, Principal principal) throws HttpMyException, IOException, UserNotFoundException, UserNotLoginException {
        userService.deleteUser(principal, userId);
        return response(OK, "User đã được xoá");
    }

    @PostMapping(value = "/danh-sach")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam(value = "search", defaultValue = "") String search,
                                                    @RequestParam("type") Integer type,
                                                    @RequestParam("pagenumber") Integer pagenumer,
                                                    Principal principal) throws Exception {
//        if (principal == null) {
//            throw new UserNotLoginException();
//        }
//
//        String currentUsername = principal.getName();
//        User user = userService.findUserAccount(currentUsername);
//
//        if (user == null) {
//            throw new UserNotFoundException("Tài khoản không tồn tại");
//        }
//
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
        return new ResponseEntity<>(userService.findByType(search, type, pagenumer, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
