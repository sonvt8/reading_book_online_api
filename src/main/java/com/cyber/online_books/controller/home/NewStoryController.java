package com.cyber.online_books.controller.home;

import com.cyber.online_books.response.HomeResponse;
import com.cyber.online_books.response.StoryTop;
import com.cyber.online_books.response.StoryUpdate;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/danh-muc/moi-cap-nhat")
public class NewStoryController {
    private final StoryService storyService;

    @Autowired
    public NewStoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping(value = "")
    public ResponseEntity<HomeResponse> getNewStory(@RequestParam(name="pagenumber") Integer pagenumber) {
        //Lấy ngày bắt đầu của tuần
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek();

        //Lấy ngày kết thúc của tuần
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek();
        //Lấy Top View Truyện Vip Trong Tuần
        List<StoryTop> topStoryWeek = storyService.
                findStoryTopViewByStatuss(ConstantsListUtils.LIST_STORY_DISPLAY, firstDayOfWeek, lastDayOfWeek,
                        ConstantsStatusUtils.HISTORY_VIEW, ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_TOP_VIEW_DEFAULT)
                .get()
                .collect(Collectors.toList());

        // Lấy Danh Sách Truyện Mới Cập Nhật
        Page< StoryUpdate > listNewStoryPage = storyService
                .findStoryUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, ConstantsUtils.PAGE_SIZE_HOME);

        HomeResponse homeResponse = new HomeResponse();
        homeResponse.setTopStoryWeek(topStoryWeek);
        homeResponse.setListNewStoryPage(listNewStoryPage);

        return new ResponseEntity<>(homeResponse, HttpStatus.OK);
    }
}
