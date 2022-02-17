package com.cyber.online_books.controller.home;

import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.response.CatalogResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/danh-muc/truyen-vip")
public class VipStoryController extends ExceptionHandling {

    private final StoryService storyService;

    @Autowired
    public VipStoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping(value = "")
    public ResponseEntity<CatalogResponse> getVipStory(@RequestParam(name="pagenumber") Integer pagenumber) {
        //Lấy ngày bắt đầu của tháng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();

        //Lấy ngày kết thúc của tháng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        //Lấy Top View Truyện Vip Trong Tháng
        List<StoryTop> topStoryMonth = storyService.
                findStoryTopViewByStatuss(ConstantsListUtils.LIST_STORY_DISPLAY, firstDayOfMonth, lastDayOfMonth,
                        ConstantsStatusUtils.HISTORY_VIEW, ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_TOP_VIEW_DEFAULT)
                .get()
                .collect(Collectors.toList());

        // Lấy Danh Sách Truyện Vip
        Page<StoryUpdate> topVipStoryPage = storyService.findStoryVipUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY, ConstantsStatusUtils.CATEGORY_ACTIVED,
                ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_DEFAULT);

        CatalogResponse catalogResponse = new CatalogResponse();
        catalogResponse.setTopStoryMonth(topStoryMonth);
        catalogResponse.setListStoryPage(topVipStoryPage);

        return new ResponseEntity<>(catalogResponse, HttpStatus.OK);
    }
}
