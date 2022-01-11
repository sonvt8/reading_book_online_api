package com.cyber.online_books.controller.account;


import com.cyber.online_books.component.MyComponent;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.EmailService;
import com.cyber.online_books.service.PayService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:messages.properties")
@RestController
@RequestMapping(value = "/api/account/pay")
public class AccountPayController {

    Logger logger = LoggerFactory.getLogger(AccountPayController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private MyComponent myComponent;

    @Value("${cyber.truyenonline.email.from}")
    private String emailForm;
    @Value("${cyber.truyenonline.email.display}")
    private String emailDisplay;
    @Value("${cyber.truyenonline.email.signature}")
    private String emailSignature;
    @Value("${cyber.truyenonline.email.url}")
    private String emailUrl;

    //Danh sách Giao dịch của người dùng
//    @PostMapping(value = "/list_pay")
//    public ResponseEntity< ? > loadListPayWithdrawOfUser(@RequestParam("pagenumber") Integer pagenumber,
//                                                         Principal principal) throws Exception {
//        if (principal == null) {
//            throw new UserNotLoginException();
//        }
//        User user = userService.findUserAccount(principal.getName());
//
//        if (user == null) {
//            throw new UserNotFoundException("Tài khoản không tồn tại");
//        }
//
//        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
//            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
//        }
//        return new ResponseEntity<>(payService.findPageByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
//    }
}
