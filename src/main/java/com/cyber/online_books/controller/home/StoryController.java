package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
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
    private final ChapterService chapterService;
    private final UserRatingService userRatingService;


    @Autowired
    public StoryController(StoryService storyService, ChapterService chapterService, UserRatingService userRatingService) {
        this.storyService = storyService;
        this.chapterService = chapterService;
        this.userRatingService = userRatingService;
    }

    @GetMapping(value = "/{storyId}")
    public ResponseEntity< ? > defaultStoryController(@PathVariable("storyId") Long storyId) throws Exception {

        return new ResponseEntity<>(getStoryDetail(storyId), HttpStatus.OK);

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

    private StoryDetailResponse getStoryDetail(Long storyId) throws Exception {
        StoryDetailResponse storyDetailResponse = new StoryDetailResponse();

        StorySummary storySummary = storyService.findStoryByStoryIdAndStatus(storyId,
                ConstantsListUtils.LIST_STORY_DISPLAY);

        Long countRating = userRatingService.countRatingStory(storyId);

        storyDetailResponse.setStorySummary(storySummary);
        storyDetailResponse.setCountRating(countRating);


        return storyDetailResponse;
    }

}
