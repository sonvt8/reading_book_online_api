package com.cyber.online_books.controller.admin;

import com.cyber.online_books.component.MyComponent;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.AdminHomeResponse;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "quan-tri")
public class AdminHomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;

    @GetMapping("")
    public ResponseEntity<AdminHomeResponse> getAllListCategories(Principal principal) throws Exception {

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
        LocalDate today = LocalDate.now();
        AdminHomeResponse adminHomeResponse = new AdminHomeResponse();

        adminHomeResponse.setNewUser(userService.countUserNewInDate(java.sql.Date.valueOf(today)));
        adminHomeResponse.setNewStory(storyService.countNewStoryInDate(java.sql.Date.valueOf(today)));
        adminHomeResponse.setNewChapter(chapterService.countNewChapterInDate(java.sql.Date.valueOf(today)));

        return new ResponseEntity<>(adminHomeResponse, HttpStatus.OK);
    }
}
