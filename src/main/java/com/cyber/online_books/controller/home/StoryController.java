package com.cyber.online_books.controller.home;

import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.response.StorySummary;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
