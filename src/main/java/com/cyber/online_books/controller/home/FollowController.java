package com.cyber.online_books.controller.home;

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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/theo-doi")
public class FollowController extends ExceptionHandling {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private StoryService storyService;

    @PostMapping(value = "/them")
    public ResponseEntity< ? > addFollow(@RequestParam("storyId") Long storyId, Principal principal)
            throws Exception {
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

        Story story = storyService.findStoryById(storyId);
        if (story == null)
            throw new HttpMyException("Truyện Không Tồn Tại hoặc Đã Bị Xóa");
        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(user.getId(), storyId);
        if (userFollow != null)
            throw new HttpMyException("Bạn đã theo dõi truyện!");

        UserFollow newUserFollow = new UserFollow();
        newUserFollow.setStory(story);
        newUserFollow.setUser(user);
        userFollowService.saveFollow(newUserFollow);
        return response(HttpStatus.OK, "Theo dõi truyện thành công");
    }

    @PostMapping(value = "/huy")
    public ResponseEntity< ? > removeFollow(@RequestParam("storyId") Long storyId, Principal principal)
            throws Exception {
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

        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(user.getId(), storyId);
        if (userFollow == null)
            throw new HttpMyException("Bạn không theo dõi truyện!");
        userFollowService.deleteFollow(userFollow);
        return response(HttpStatus.OK, "Hủy theo dõi truyện thành công");
    }

    @PostMapping(value = "/kiem-tra")
    public ResponseEntity< ? > checkFollowStoryOfUser(Principal principal, @RequestParam("storyId") Long storyId)
            throws Exception {
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

        return new ResponseEntity<>(userFollowService.existsUserFollow(user.getId(), storyId), HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}
