package com.cyber.online_books.controller.admin;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Set;

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

    @PostMapping("/sua-truyen/{id}")
    public ResponseEntity<Story> addStory(@RequestParam("name") String name,
                                          @RequestParam("author") String author,
                                          @RequestParam("infomation") String infomation,
                                          @RequestParam("category") Set<String> category,
                                          @RequestParam(value="image") MultipartFile image,
                                          @RequestParam("price") Double price,
                                          @RequestParam("timeDeal") Integer timeDeal,
                                          @RequestParam("dealStatus") Integer dealStatus,
                                          Principal principal,
                                          @PathVariable(value = "id") Long id) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story updateStory = storyService.updateAdminStory(id, name, author, infomation, category.toArray(new String[0]), image, price, timeDeal, dealStatus, principal);
        return new ResponseEntity<>(updateStory, HttpStatus.OK);
    }

    @DeleteMapping("/xoa-truyen/{id}")
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
