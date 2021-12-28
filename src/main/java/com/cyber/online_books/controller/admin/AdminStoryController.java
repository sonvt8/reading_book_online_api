package com.cyber.online_books.controller.admin;

import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quan-tri/truyen")
public class AdminStoryController {

    private final StoryService storyService;
    private final UserService userService;

    @Autowired
    public AdminStoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @PostMapping(value = "/danh-sach")
    public ResponseEntity< ? > loadStoryAdmin(@RequestParam("pagenumber") Integer pagenumber, @RequestParam("search") String search,
                                              @RequestParam("type") Integer type) {
        return new ResponseEntity<>(storyService.findStoryInAdmin(pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT, type, search), HttpStatus.OK);
    }
}
