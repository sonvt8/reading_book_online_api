package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/follow")
public class FollowController extends ExceptionHandling {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private StoryService storyService;

    @GetMapping(value = "/followOfUser")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("pagenumber") Integer pagenumber, Principal principal)
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
}
