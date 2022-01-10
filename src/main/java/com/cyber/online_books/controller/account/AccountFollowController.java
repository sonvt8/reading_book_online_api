package com.cyber.online_books.controller.account;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.entity.UserFollow;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserFollowService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/tai-khoan/theo-doi")
public class AccountFollowController extends ExceptionHandling {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;

    @GetMapping(value = "/danh-sach")
    public ResponseEntity< ? > listFollow(@RequestParam("pagenumber") Integer pagenumber, Principal principal)
            throws UserNotLoginException, HttpMyException, UserNotFoundException {
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

        return new ResponseEntity<>(userFollowService.findAllStoryFollowByUserId(user.getId(), pagenumber,
                ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }

    @DeleteMapping(value = "/huy/{storyId}/{userId}")
    public ResponseEntity<HttpResponse> cancelFollow(@PathVariable("storyId") Long storyId, @PathVariable("userId") Long userId,
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
        if (user.getId() != userId)
            throw new HttpMyException("Bạn không có quyền hủy theo dõi truyện không phải của bản thân!");

        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(userId, storyId);
        if (userFollow == null)
            throw new HttpMyException("Bạn không theo dõi truyện!");

        userFollowService.deleteFollow(userFollow);

        return response(HttpStatus.OK, "Hủy theo dõi thành công");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
