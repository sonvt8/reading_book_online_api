package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.ChapterResponse;
import com.cyber.online_books.service.*;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
    private final HistoryService historyService;
    private final PayService payService;

    @Autowired
    public ChapterController(StoryService storyService, UserService userService, ChapterService chapterService, HistoryService historyService, PayService payService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
        this.historyService = historyService;
        this.payService = payService;
    }

    @GetMapping("/{sID}/chuong-{chID}")
    public ResponseEntity< ? > chapterPage(@PathVariable("sID") Long sid,
                                           @PathVariable("chID") Long chid,
                                           Principal principal,
                                           HttpServletRequest request) throws Exception {

        ChapterResponse chapterResponse = new ChapterResponse();

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

        //Lấy Chapter Theo sID và chID
        Chapter chapter = chapterService.findChapterByStoryIdAndChapterID(sid, ConstantsListUtils.LIST_STORY_DISPLAY,
                chid, ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        Long preChapter = chapterService.findPreviousChapterID(chapter.getSerial(), chapter.getStory().getId());
        Long nextChapter = chapterService.findNextChapterID(chapter.getSerial(), chapter.getStory().getId());

        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();

        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);

        // Lấy thời gian 30 phút trước
        Date halfHourAgo = DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR);

        //Lấy LocationIP Client
        String locationIP = getLocationIP(request);

        boolean checkVip = checkVipStory(user, chapter, dayAgo, now);

        //Lưu Lịch Sử Đọc Truyện
        saveFavorites(user, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }

        chapterResponse.setChapter(chapter);
        chapterResponse.setPreChapter(preChapter);
        chapterResponse.setNextChapter(nextChapter);
        chapterResponse.setCheckVip(checkVip);

        return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
    }

    private boolean checkVipStory(User user,
                               Chapter chapter,
                               Date dayAgo,
                               Date now) {
        boolean check = true;

        //Kiểm Tra Chapter có phải tính phí hay không
        //Chapter tính phí là chapter có chStatus = 2
        if (chapter.getStatus() == 2) {

            // Kiểm tra người dùng đã đăng nhập chưa
            if (user != null) {
                //Kiểm tra người dùng có phải người đăng chapter không
                boolean checkUser = user.equals(chapter.getUser());
                // Kiểm tra người dùng đã thanh toán chương vip trong 24h qua không
                // Nếu chưa thanh toán rồi thì check = false
                if (!checkUser) {
                    boolean checkPay = checkDealStory(chapter.getId(), user.getId(), dayAgo, now);
                    if (!checkPay) {
                        check = false;
                    }
                }
            } else {
                check = false;
            }
        }
        return check;
    }

    private boolean checkDealStory(Long chID,
                                   Long uID,
                                   Date dayAgo,
                                   Date now) {
        boolean check = payService.checkDealChapterVip(chID, uID, dayAgo, now);
        return check;
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

    private void saveFavorites(User user,
                               Chapter chapter,
                               Date halfHourAgo,
                               Date now,
                               String LocationIP) throws Exception {
        // Kiểm Tra đã đọc Chapter trong Khoảng
        boolean check = historyService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;

        if (check) {
            uView = 0;
        } else {
            // Chưa Đọc Chapter Trong Khoảng 30 phút Thì Tăng Lượt View Của Chapter
            chapterService.updateViewChapter(chapter);
        }
        historyService.saveHistory(chapter, user, LocationIP, uView);
    }

}
