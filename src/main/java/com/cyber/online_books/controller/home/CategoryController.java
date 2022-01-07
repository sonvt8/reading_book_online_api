package com.cyber.online_books.controller.home;

import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.NotFoundException;
import com.cyber.online_books.response.CategorySummary;
import com.cyber.online_books.response.StoryByCategoryId;
import com.cyber.online_books.response.StoryTop;
import com.cyber.online_books.response.StoryUpdate;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/the-loai")
public class CategoryController extends ExceptionHandling {

    private final CategoryService categoryService;
    private final StoryService storyService;
    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService, StoryService storyService) {
        this.categoryService = categoryService;
        this.storyService = storyService;
    }

    @GetMapping("/danh-sach")
    public ResponseEntity<?> getAllCategories(){
        List<CategorySummary> listCategories = categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED);

        return ResponseEntity.status(HttpStatus.OK).body(listCategories);
    }

    @GetMapping("/{cid}")
    public ResponseEntity<?> getStoriesByCategoryId(@PathVariable("cid") String cid,
                                                    @RequestParam(name="pagenumber") Integer pagenumber) throws Exception {

        StoryByCategoryId story = new StoryByCategoryId();

        CategorySummary categoryResponse = checkCategoryID(cid);
        Page<StoryUpdate> pageStory = storyService
                .findStoryNewUpdateByCategoryId(categoryResponse.getId(),
                        pagenumber,
                        ConstantsUtils.PAGE_SIZE_DEFAULT,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        //Lấy ngày bắt đầu của tháng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();

        //Lấy ngày bắt đầu của tuần
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek();

        //Lấy ngày kết thúc của tháng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        //Lấy ngày kết thúc của tuần
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek();

        //Lấy Top View Truyện Theo Thể Loại Trong tuần
        List<StoryTop> listTopViewWeek = storyService
                .findStoryTopViewByCategoryId(categoryResponse.getId(),
                        ConstantsStatusUtils.HISTORY_VIEW,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        firstDayOfWeek, lastDayOfWeek,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .get()
                .collect(Collectors.toList());

        //Lấy Top Truyện Đề cử Theo Thể Loại Trong Tháng
        List< StoryTop > listTopAppointMonth = storyService
                .findStoryTopVoteByCategoryId(categoryResponse.getId(),
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsPayTypeUtils.PAY_APPOINT_TYPE, ConstantsStatusUtils.PAY_COMPLETED,
                        firstDayOfMonth, lastDayOfMonth,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .get()
                .collect(Collectors.toList());

        story.setPageStory(pageStory);
        story.setListTopViewWeek(listTopViewWeek);
        story.setListTopAppointMonth(listTopAppointMonth);

        return ResponseEntity.status(HttpStatus.OK).body(story);
    }

    private CategorySummary checkCategoryID(String cid) throws Exception {

        // Kiểm tra cid != null
        // Kiểm tra cid có phải kiểu int
        if (cid == null || !WebUtils.checkIntNumber(cid)) {
            throw new NotFoundException();
        }

        return categoryService.getCategoryByID(Integer.parseInt(cid), ConstantsStatusUtils.CATEGORY_ACTIVED);
    }
}
