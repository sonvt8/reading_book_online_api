package com.cyber.online_books.controller.account;


import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.service.CloudinaryService;
import com.cyber.online_books.service.PayService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsPayTypeUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai_khoan")
public class AccountUserController {
    private final static Logger logger = LoggerFactory.getLogger(AccountUserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;

    @PostMapping(value = "/doi_ngoai_hieu")
    @Transactional
    public ResponseEntity<User> changeNick(@RequestParam(value = "newNick") String newNick,
                                           Principal principal) throws HttpMyException, UserNotFoundException {
        User user = userService.updateDisplayName(principal,newNick);
        payService.savePay(null, null, user, null, 0,
                ConstantsUtils.PRICE_UPDATE_NICK, ConstantsPayTypeUtils.PAY_DISPLAY_NAME_TYPE);
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping(value = "/doi_mat_khau")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam("old-pass")String oldPassword,
                                                       @RequestParam("new-pass")String newPassword,
                                                       Principal principal) throws UserNotFoundException, HttpMyException {
        userService.updatePassword(oldPassword, newPassword,principal);
        return response(OK, "Đã cập nhật thành công mật khẩu mới");
    }

    @PostMapping(value = "/doi_thong_bao")
    public ResponseEntity<User> changeNotification(@RequestParam("notification")String newMess, Principal principal) throws UserNotFoundException {
        User user = userService.updateNotification(principal,newMess.trim());
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/anh_dai_dien")
    @ResponseBody
    public ResponseEntity<User> changeAvatar(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage, Principal principal) throws NotAnImageFileException, UserNotFoundException {
        User user = userService.updateAvatar(principal,profileImage);
        return new ResponseEntity<>(user, OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
