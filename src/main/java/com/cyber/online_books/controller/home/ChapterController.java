package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/chuong")
public class ChapterController extends ExceptionHandling {

    private final StoryService storyService;
    private final UserService userService;
    private final ChapterService chapterService;

    @Autowired
    public ChapterController(StoryService storyService, UserService userService, ChapterService chapterService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
    }



    // Kiểm Tra Người Dùng Đã Đăng Nhập Chưa
    private User checkUserLogin(Principal principal) {
        User user = null;
        if (principal != null) {
            // Lấy Người Dùng đang đăng nhập
            user = userService.findUserAccount(principal.getName());
        }
        return user;
    }

    //Lấy Địa Chỉ Ip client
    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";

        //Kiểm Tra HttpServletRequest có null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

}
