package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Category;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Integer id) {
        Category category = categoryService.findCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/danh-sach-truyen/{cid}")
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

        //L???y ng??y b???t ?????u c???a th??ng
        Date firstDayOfMonth = DateUtils.getFirstDayOfMonth();

        //L???y ng??y b???t ?????u c???a tu???n
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek();

        //L???y ng??y k???t th??c c???a th??ng
        Date lastDayOfMonth = DateUtils.getLastDayOfMonth();

        //L???y ng??y k???t th??c c???a tu???n
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek();

        //L???y Top View Truy???n Theo Th??? Lo???i Trong tu???n
        List<StoryTop> listTopViewWeek = storyService
                .findStoryTopViewByCategoryId(categoryResponse.getId(),
                        ConstantsStatusUtils.HISTORY_VIEW,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        firstDayOfWeek, lastDayOfWeek,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE)
                .get()
                .collect(Collectors.toList());

        //L???y Top Truy???n ????? c??? Theo Th??? Lo???i Trong Th??ng
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

        // Ki???m tra cid != null
        // Ki???m tra cid c?? ph???i ki???u int
        if (cid == null || !WebUtils.checkIntNumber(cid)) {
            throw new NotFoundException();
        }

        return categoryService.getCategoryByID(Integer.parseInt(cid), ConstantsStatusUtils.CATEGORY_ACTIVED);
    }
}
