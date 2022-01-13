package com.cyber.online_books.controller.account;


import com.cyber.online_books.component.MyComponent;
import com.cyber.online_books.entity.Mail;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:messages.properties")
@RestController
@RequestMapping(value = "/tai-khoan/thanh-toan")
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
    @GetMapping(value = "/danh-sach")
    public ResponseEntity< ? > loadListPayOfUser(@RequestParam("pagenumber") Integer pagenumber,
                                                         Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        return new ResponseEntity<>(payService.findPageByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    //Danh sách giao dịch rút tiền của người dùng
    @GetMapping(value = "/danh-sach-rut-tien")
    public ResponseEntity< ? > loadListPayWithdrawOfUser(@RequestParam("pagenumber") Integer pagenumber,
                                                 Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        return new ResponseEntity<>(payService.findPagePayWithdrawByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    //THực Hiện Đăng Ký Rút Tiền
    @PostMapping(value = "/rut-tien")
    public ResponseEntity< ? > submitPayDraw(@RequestParam("money") Double money, @RequestParam("moneyVND") Double vnd,
                                             Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        if (vnd % 10000 != 0 && vnd < 50000)
            throw new HttpMyException("Số Tiền Cần đổi phải chia hết cho 10000! Rút ít nhất 50000VND");
        if (user.getGold() < money)
            throw new HttpMyException(" Số dư tài khoản bạn không đủ!");
        try {
            Long result = payService.savePayDraw(user, money);
            Mail mail = new Mail();
            mail.setFrom(emailForm);
            mail.setTo(user.getEmail());
            mail.setSubject("Thông báo đăng ký rút tiền!");
            mail.setFromDisplay(emailDisplay);
            Map< String, Object > modelMap = new HashMap< String, Object >();
            modelMap.put("name", user.getDisplayName() != null ? user.getDisplayName() : user.getUsername());
            modelMap.put("url", emailUrl);
            modelMap.put("signature", emailSignature);
            modelMap.put("pay", payService.findPayById(result));
            mail.setModel(modelMap);
            emailService.sendSimpleMessage(mail, "mail/withdraw-money");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new HttpMyException("Có lỗi xảy ra! Hãy Thực hiện lai sau!");
        }
    }
}
