package com.cyber.online_books.controller.account;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.StoryUser;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.data.domain.Page;
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
    private final UserService userService;

    public AccountStoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @GetMapping(value = "/list")
    public ResponseEntity< ? > getStoryByAccount(@RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("status") int status,
                                                 Principal principal) throws UserNotLoginException {
        if (principal == null) {
            throw new UserNotLoginException();
        }

        User user = userService.findUserAccount(principal.getName());
        Page<StoryUser> pageStory = storyService.findPageStoryByUser(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT, status);
        return new ResponseEntity<>(pageStory, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Story> addStory(@RequestParam("name") String name,
                           @RequestParam("author") String author,
                           @RequestParam("infomation") String infomation,
                           @RequestParam("category") Set<String> category,
                           @RequestParam(value="image") MultipartFile image,
                           Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story newStory = storyService.addNewStory(name, author, infomation, category.toArray(new String[0]), image, principal);
        return new ResponseEntity<>(newStory, HttpStatus.OK);
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
        return new ResponseEntity<>(updateStory, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteStory(@PathVariable("id") Long id, Principal principal) throws HttpMyException, UserNotLoginException {
        Story story = storyService.findStoryById(id);
        if (principal == null) {
            throw new UserNotLoginException();
        }
        if(story == null)
            throw new HttpMyException("Not found Story for delete");
        boolean result = storyService.deleteStory(id);
        if(result)
            return response(HttpStatus.OK, "Story deleted successfully");
        else
            throw new HttpMyException("Can not delete this Story");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
