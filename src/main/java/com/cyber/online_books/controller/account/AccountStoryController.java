package com.cyber.online_books.controller.account;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.StoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping(value = "api/account/story")
public class AccountStoryController extends ExceptionHandling {

    private final StoryService storyService;

    public AccountStoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @PostMapping("/add")
    public ResponseEntity<Story> addStory(@RequestParam("name") String name,
                           @RequestParam("author") String author,
                           @RequestParam("infomation") String infomation,
                           @RequestParam("category") Set<String> category,
                           @RequestParam(value="image", required = false) MultipartFile image,
                           Principal principal) throws HttpMyException, UserNotLoginException {
        Story newStory = storyService.addNewStory(name, author, infomation, category.toArray(new String[0]), image, principal);
        return new ResponseEntity<Story>(newStory, HttpStatus.OK);
    }
}
