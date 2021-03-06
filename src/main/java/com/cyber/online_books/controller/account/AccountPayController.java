package com.cyber.online_books.controller.account;


import com.cyber.online_books.component.MyComponent;
import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Mail;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.EmailService;
import com.cyber.online_books.service.PayService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
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

    //Danh s??ch Giao d???ch c???a ng?????i d??ng
    @GetMapping(value = "/danh-sach")
    public ResponseEntity< ? > loadListPayOfUser(@RequestParam("pagenumber") Integer pagenumber,
                                                         Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        return new ResponseEntity<>(payService.findPageByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    //Danh s??ch giao d???ch r??t ti???n c???a ng?????i d??ng
    @GetMapping(value = "/danh-sach-rut-tien")
    public ResponseEntity< ? > loadListPayWithdrawOfUser(@RequestParam("pagenumber") Integer pagenumber,
                                                 Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        return new ResponseEntity<>(payService.findPagePayWithdrawByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    //TH???c Hi???n ????ng K?? R??t Ti???n
    @PostMapping(value = "/rut-tien")
    public ResponseEntity< ? > submitPayDraw(@RequestParam("money") Double money, @RequestParam("moneyVND") Double vnd,
                                             Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        if (vnd % 10000 != 0 && vnd < 50000)
            throw new HttpMyException("S??? Ti???n C???n ?????i ph???i chia h???t cho 10000! R??t ??t nh???t 50000VND");
        if (user.getGold() < money)
            throw new HttpMyException(" S??? d?? t??i kho???n b???n kh??ng ?????!");
        try {
            Long result = payService.savePayDraw(user, money);
            Mail mail = new Mail();
            mail.setFrom(emailForm);
            mail.setTo(user.getEmail());
            mail.setSubject("Th??ng b??o ????ng k?? r??t ti???n!");
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
            throw new HttpMyException("C?? l???i x???y ra! H??y Th???c hi???n lai sau!");
        }
    }

    @PostMapping(value = "/mua-chapter-vip")
    @Transactional
    public ResponseEntity< HttpResponse > payReadingChapter(@RequestParam(value = "chapterId") String chapterId, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        if (chapterId == null || WebUtils.checkLongNumber(chapterId)) {
            throw new HttpMyException("C?? l???i x???y ra! Mong b???n quay l???i sau.");
        }
        Chapter chapter = chapterService
                .findChapterByIdAndStatus(Long.valueOf(chapterId), ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        if (chapter == null) {
            throw new HttpMyException("Kh??ng t???n t???i ch????ng truy???n n??y!");
        }
        if (chapter.getStatus() == 1) {
            return response(HttpStatus.OK,"");
        }
        //L???y Th???i Gian Hi???n T???i
        Date now = DateUtils.getCurrentDate();

        // L???y Th???i Gian 24h Tr?????c
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);
        boolean check = payService.checkDealChapterVip(Long.valueOf(chapterId), user.getId(), dayAgo, now);
        logger.info("Id chapter: " + chapterId);
        logger.info("Th???i gian ki???m tra: " + dayAgo + " - " + now);
        logger.info("Ki???m Tra: " + check);
        if (check) {
            return response(HttpStatus.OK,"");
        }
        if (user.getGold() < chapter.getPrice()) {
            throw new HttpMyException("S??? d?? trong t??i kho???n kh??ng ????? ????? thanh to??n!");
        }
        try {
            payService.saveReadingVipPay(user, chapter);
            return response(HttpStatus.OK,"Mua ch????ng th??nh c??ng");
        } catch (Exception e) {
            throw new HttpMyException("C?? l???i x???y ra. Vui l??ng th??? l???i sau");
        }
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
