package com.cyber.online_books.controller.account;


import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.response.InfoSummary;
import com.cyber.online_books.service.CloudinaryService;
import com.cyber.online_books.service.PayService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsPayTypeUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static com.cyber.online_books.utils.UserImplContant.NO_USER_FOUND_BY_USERNAME;
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
    @Autowired
    private StoryService storyService;

    @GetMapping(value = "")
    public ResponseEntity<InfoSummary> accountInfo(Principal principal) throws Exception  {
        User currentUser = validatePricipal(principal);

        InfoSummary accInfo = userService.findInfoUserById(currentUser.getId());

        return new ResponseEntity<>(accInfo, OK);
    }

    @PostMapping(value = "/doi_ngoai_hieu")
    @Transactional
    public ResponseEntity<User> changeNick(@RequestParam(value = "newNick") String newNick,
                                           Principal principal) throws HttpMyException, UserNotFoundException {
        User currentUser = validatePricipal(principal);
        User user = userService.updateDisplayName(principal,newNick);
        payService.savePay(null, null, user, null, 0,
                ConstantsUtils.PRICE_UPDATE_NICK, ConstantsPayTypeUtils.PAY_DISPLAY_NAME_TYPE);
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping(value = "/doi_mat_khau")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam("old-pass")String oldPassword,
                                                       @RequestParam("new-pass")String newPassword,
                                                       Principal principal) throws UserNotFoundException, HttpMyException {
        User currentUser = validatePricipal(principal);
        userService.updatePassword(oldPassword, newPassword,principal);
        return response(OK, "Đã cập nhật thành công mật khẩu mới");
    }

    @PostMapping(value = "/doi_thong_bao")
    public ResponseEntity<User> changeNotification(@RequestParam("notification")String newMess, Principal principal) throws UserNotFoundException {
        User currentUser = validatePricipal(principal);
        User user = userService.updateNotification(principal,newMess.trim());
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/anh_dai_dien")
    @ResponseBody
    public ResponseEntity<User> changeAvatar(@RequestParam(value = "profileImage", required = false) MultipartFile profileImage, Principal principal) throws HttpMyException, NotAnImageFileException, UserNotFoundException {
        User currentUser = validatePricipal(principal);
        if (profileImage.getSize() > (20 * 1024 * 1024)) {
            throw new HttpMyException("Kích thước ảnh upload tối đa là 20 Megabybtes!");
        }
        User user = userService.updateAvatar(principal,profileImage);
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping(value = "/de_cu_truyen")
    public ResponseEntity<HttpResponse> appoindStory(@RequestParam("storyId") Long storyId,
                                            @RequestParam("coupon") Integer coupon,
                                            Principal principal) throws Exception {
        User currentUser = validatePricipal(principal);
        Story story = storyService.findStoryByIdAndStatus(storyId, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            throw new HttpMyException("Truyện không tồn tại hoặc đã bị xóa!");
        }
        if (coupon <= 0) {
            throw new HttpMyException("Số phiếu đề cử ít nhất là 1!");
        }
        if (currentUser.getGold() < (coupon * 1000))
            throw new HttpMyException("Số dư của bạn không đủ để đề cử");
        boolean check = payService.savePayAppoint(story,  currentUser, (double) (coupon * 1000), coupon, ConstantsPayTypeUtils.PAY_APPOINT_TYPE);
        if (check)
            return response(OK, "Bạn đã thực hiện đề cử thành công!");
        else
            throw new HttpMyException("Có lỗi xảy ra mong bạn quay lại sau!");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private User validatePricipal(Principal principal) throws UserNotFoundException {
        String currentUsername = principal.getName();
        User currentUser = userService.findUserAccount(currentUsername);
        if(currentUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        }
        if (currentUser.getStatus() != 1){
            throw new DisabledException("Tài khoản đã bị đóng hoặc chưa được kích hoạt");
        }
        return currentUser;
    }
}
