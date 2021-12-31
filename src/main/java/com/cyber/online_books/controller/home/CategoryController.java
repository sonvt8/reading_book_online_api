package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.exception.domain.NotFoundException;
import com.cyber.online_books.response.CategoryResponse;
import com.cyber.online_books.response.StoryUpdate;
import com.cyber.online_books.service.CategoryService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/the-loai")
public class CategoryController {

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
        List<CategoryResponse> listCategories = categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED);

        return ResponseEntity.status(HttpStatus.OK).body(listCategories);
    }

    @GetMapping("/{cid}")
    public ResponseEntity<?> getStoriesByCategoryId(@PathVariable("cid") String cid,
                                                    @RequestParam(name="pagenumber") Integer pagenumber) throws Exception {

        CategoryResponse categoryResponse = checkCategoryID(cid);
        Page<StoryUpdate> pageStory = storyService
                .findStoryNewUpdateByCategoryId(categoryResponse.getId(),
                        pagenumber,
                        ConstantsUtils.PAGE_SIZE_DEFAULT,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        return ResponseEntity.status(HttpStatus.OK).body(pageStory);
    }

    private CategoryResponse checkCategoryID(String cid) throws Exception {

        // Kiểm tra cid != null
        // Kiểm tra cid có phải kiểu int
        if (cid == null || !WebUtils.checkIntNumber(cid)) {
            throw new NotFoundException();
        }

        return categoryService.getCategoryByID(Integer.parseInt(cid), ConstantsStatusUtils.CATEGORY_ACTIVED);
    }
}
