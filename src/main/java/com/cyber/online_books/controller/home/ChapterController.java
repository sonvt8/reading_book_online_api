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
                                           HttpServletRequest request) throws Exception {

        ChapterResponse chapterResponse = new ChapterResponse();

        //L???y Chapter Theo sID v?? chID
        Chapter chapter = chapterService.findChapterByStoryIdAndChapterID(sid, ConstantsListUtils.LIST_STORY_DISPLAY,
                chid, ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        Long preChapter = chapterService.findPreviousChapterID(chapter.getSerial(), chapter.getStory().getId());
        Long nextChapter = chapterService.findNextChapterID(chapter.getSerial(), chapter.getStory().getId());

        //L???y Th???i Gian Hi???n T???i
        Date now = DateUtils.getCurrentDate();

        // L???y Th???i Gian 24h Tr?????c
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);

        // L???y th???i gian 30 ph??t tr?????c
        Date halfHourAgo = DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR);

        //L???y LocationIP Client
        String locationIP = getLocationIP(request);

        //L??u L???ch S??? ?????c Truy???n
        saveFavorites(null, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }

        boolean checkVip = checkVipStory(null, chapter, dayAgo, now);

        chapterResponse.setChapter(chapter);
        chapterResponse.setPreChapter(preChapter);
        chapterResponse.setNextChapter(nextChapter);
        chapterResponse.setCheckVip(checkVip);
        chapterResponse.setTimeDealDay(DateUtils.betweenTwoDays2(chapter.getDealine()));

        return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
    }

    //L???y ?????a Ch??? Ip client
    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";

        //Ki???m Tra HttpServletRequest c?? null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    private boolean checkVipStory(User user,
                                  Chapter chapter,
                                  Date dayAgo,
                                  Date now) {
        boolean check = true;

        //Ki???m Tra Chapter c?? ph???i t??nh ph?? hay kh??ng
        //Chapter t??nh ph?? l?? chapter c?? chStatus = 2
        if (chapter.getStatus() == 2) {
            if(DateUtils.betweenTwoDays2(chapter.getDealine())>0){
                check = true;
            } else {
                check = false;
            }
        }
        return check;
    }

    private void saveFavorites(User user,
                               Chapter chapter,
                               Date halfHourAgo,
                               Date now,
                               String LocationIP) throws Exception {
        // Ki???m Tra ???? ?????c Chapter trong Kho???ng
        boolean check = historyService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;

        if (check) {
            uView = 0;
        } else {
            // Ch??a ?????c Chapter Trong Kho???ng 30 ph??t Th?? T??ng L?????t View C???a Chapter
            chapterService.updateViewChapter(chapter);
        }
        historyService.saveHistory(chapter, user, LocationIP, uView);
    }

}
