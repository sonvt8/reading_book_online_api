package com.cyber.online_books.controller.account;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.StoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                           @RequestParam(value="image") MultipartFile image,
                           Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story newStory = storyService.addNewStory(name, author, infomation, category.toArray(new String[0]), image, principal);
        return new ResponseEntity<Story>(newStory, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Story> updateStory(@RequestParam("name") String name,
                                          @RequestParam("author") String author,
                                          @RequestParam("infomation") String infomation,
                                          @RequestParam("category") Set<String> category,
                                          @RequestParam(value="image") MultipartFile image,
                                          @PathVariable(value = "id") Long id,
                                          Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story updateStory = storyService.updateAccountStory(id, name, author, infomation, category.toArray(new String[0]), image, principal);
        return new ResponseEntity<Story>(updateStory, HttpStatus.OK);
    }
}
