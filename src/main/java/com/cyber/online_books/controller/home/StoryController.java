package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.response.*;
import com.cyber.online_books.service.*;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/truyen-home")
public class StoryController extends ExceptionHandling {

    private final StoryService storyService;
    private final UserService userService;
    private final ChapterService chapterService;
    private final UserRatingService userRatingService;
    private final HistoryService historyService;

    @Autowired
    public StoryController(StoryService storyService, UserService userService, ChapterService chapterService, UserRatingService userRatingService, HistoryService historyService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
        this.userRatingService = userRatingService;
        this.historyService = historyService;
    }

    @GetMapping(value = "/{storyId}")
    public ResponseEntity< ? > defaultStoryController(@PathVariable("storyId") Long storyId, Principal principal) throws Exception {

        return new ResponseEntity<>(getStoryDetail(storyId, principal), HttpStatus.OK);

    }

    @GetMapping(value = "/{storyId}/chuong-cua-truyen")
    public ResponseEntity< ? > loadChapterOfStory(@PathVariable("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        // lấy danh sách chương có phân trang
        Page<ChapterOfStory> chapterOfStoryPage = chapterService
                .getListChapterOfStory(storyId, pagenumber, ConstantsListUtils.LIST_CHAPTER_DISPLAY, type);
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }

    //Lấy Top 5 Truyện mới đăng của Converter
    @GetMapping(value = "/truyen-cua-converter")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam("userId") Long userId) {
        List<StorySlide> list = storyService
                .findStoryOfConverter(userId, ConstantsListUtils.LIST_STORY_DISPLAY);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/truyen-cua-thanh-vien")
    public ResponseEntity< ? > loadStoryOfMember(@RequestParam("userId") Long userId,
                                                 @RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("type") int type) {
        Page<StoryMember> storyMembers = storyService
                .findStoryByUserId(userId, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, type, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(storyMembers, HttpStatus.OK);
    }

    private StoryDetailResponse getStoryDetail(Long storyId, Principal principal) throws Exception {
        StoryDetailResponse storyDetailResponse = new StoryDetailResponse();

        StorySummary storySummary = storyService.findStoryByStoryIdAndStatus(storyId,
                ConstantsListUtils.LIST_STORY_DISPLAY);

        boolean checkRating = false;
        boolean checkConverter = false;
        Chapter chapter = null;

        if(principal != null){
            String currentUsername = principal.getName();
            User user = userService.findUserAccount(currentUsername);
            if (user != null) {
                chapter = historyService.findChapterReadByUser(user.getId(), storyId);
                checkConverter = Objects.equals(user.getId(), storySummary.getUserId());
                //Nếu người đọc là người đăng thì tính là đã đánh giá
                if (storySummary.getUserId().equals(user.getId())) {
                    // Người đọc là Converter
                    checkRating = true;
                } else {

                    // Kiểm tra Người dùng đã đánh giá chưa
                    if (userRatingService.existsRatingWithUser(storyId, user.getId())) {
                        // Người dùng đã đánh giá
                        checkRating = true;
                    }
                }
            }
        }

        Long countRating = userRatingService.countRatingStory(storyId);

        storyDetailResponse.setReadChapter(chapter);
        storyDetailResponse.setStorySummary(storySummary);
        storyDetailResponse.setCountRating(countRating);
        storyDetailResponse.setRating(checkRating);
        storyDetailResponse.setCheckConverter(checkConverter);

        return storyDetailResponse;
    }

}
