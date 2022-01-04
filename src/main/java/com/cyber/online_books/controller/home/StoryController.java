package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.*;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/truyen")
public class StoryController extends ExceptionHandling {

    private final StoryService storyService;
    private final UserService userService;
    private final ChapterService chapterService;

    @Autowired
    public StoryController(StoryService storyService, UserService userService, ChapterService chapterService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
    }

    @GetMapping(value = "/{storyId}")
    public ResponseEntity< ? > defaultStoryController(@PathVariable("storyId") Long storyId) throws Exception {

        // lấy truyện theo id va status
        StorySummary storySummary = storyService.findStoryByStoryIdAndStatus(storyId,
                ConstantsListUtils.LIST_STORY_DISPLAY);

        return new ResponseEntity<>(storySummary, HttpStatus.OK);
    }

    @GetMapping(value = "/{storyId}/chapterOfStory")
    public ResponseEntity< ? > loadChapterOfStory(@PathVariable("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        // lấy danh sách chương có phân trang
        Page<ChapterOfStory> chapterOfStoryPage = chapterService
                .getListChapterOfStory(storyId, pagenumber, ConstantsListUtils.LIST_CHAPTER_DISPLAY, type);
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }

    //Lấy Top 5 Truyện mới đăng của Converter
    @GetMapping(value = "/storyOfConverter")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam("userId") Long userId) {
        List<StorySlide> list = storyService
                .findStoryOfConverter(userId, ConstantsListUtils.LIST_STORY_DISPLAY);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/storyOfMember")
    public ResponseEntity< ? > loadStoryOfMember(@RequestParam("userId") Long userId,
                                                 @RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("type") int type) {
        Page<StoryMember> storyMembers = storyService
                .findStoryByUserId(userId, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, type, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(storyMembers, HttpStatus.OK);
    }

}
