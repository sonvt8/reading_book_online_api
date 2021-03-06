package com.cyber.online_books.controller.account;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.CheckStoryDetail;
import com.cyber.online_books.response.StorySummary;
import com.cyber.online_books.response.StoryUser;
import com.cyber.online_books.service.HistoryService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserRatingService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "tai-khoan/truyen")
public class AccountStoryController extends ExceptionHandling {

    private final StoryService storyService;
    private final UserService userService;
    private final HistoryService historyService;
    private final UserRatingService userRatingService;

    @Autowired
    public AccountStoryController(StoryService storyService, UserService userService, HistoryService historyService, UserRatingService userRatingService) {
        this.storyService = storyService;
        this.userService = userService;
        this.historyService = historyService;
        this.userRatingService = userRatingService;
    }

    @GetMapping(value = "/danh-sach")
    public ResponseEntity< ? > getStoryByAccount(@RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("status") int status,
                                                 Principal principal) throws UserNotLoginException, HttpMyException, UserNotFoundException {
        if (principal == null) {
            throw new UserNotLoginException();
        }

        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }
        Page<StoryUser> pageStory = storyService.findPageStoryByUser(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT, status);
        return new ResponseEntity<>(pageStory, HttpStatus.OK);
    }

    @PostMapping("/them-truyen")
    public ResponseEntity<Story> addStory(@RequestParam("name") String name,
                           @RequestParam("author") String author,
                           @RequestParam("infomation") String infomation,
                           @RequestParam("category") Set<String> category,
                           @RequestParam(value="image") MultipartFile image,
                           Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story newStory = storyService.addNewStory(name, author, infomation, category.toArray(new String[0]), image, principal);
        return new ResponseEntity<>(newStory, HttpStatus.OK);
    }

    @PostMapping("/sua-truyen/{id}")
    public ResponseEntity<Story> updateStory(@RequestParam("name") String name,
                                          @RequestParam("author") String author,
                                          @RequestParam("infomation") String infomation,
                                          @RequestParam("category") Set<String> category, @RequestParam("status") Integer status,
                                          @RequestParam(value="image", required = false) MultipartFile image,
                                          @PathVariable(value = "id") Long id,
                                          Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story updateStory = storyService.updateAccountStory(id, name, author, infomation, category.toArray(new String[0]), status, image, principal);
        return new ResponseEntity<>(updateStory, HttpStatus.OK);
    }

    @DeleteMapping("/xoa-truyen/{id}")
    public ResponseEntity<HttpResponse> deleteStory(@PathVariable("id") Long id, Principal principal) throws HttpMyException, UserNotLoginException {
        Story story = storyService.findStoryById(id);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        if(story == null)
            throw new HttpMyException("kh??ng t??m th???y truy???n");
        boolean result = storyService.deleteStory(id);
        if(result)
            return response(HttpStatus.OK, "truy???n x??a th??nh c??ng");
        else
            throw new HttpMyException("kh??ng th??? x??a truy???n n??y");
    }

    @GetMapping(value = "/kiem-tra/{storyId}")
    public ResponseEntity< CheckStoryDetail > checkDefaultStoryController(@PathVariable("storyId") Long storyId, Principal principal) throws Exception {
        CheckStoryDetail checkStoryDetail = new CheckStoryDetail();

        if (principal == null) {
            throw new UserNotLoginException();
        }

        StorySummary storySummary = storyService.findStoryByStoryIdAndStatus(storyId,
                ConstantsListUtils.LIST_STORY_DISPLAY);
        boolean checkRating = false;
        boolean checkConverter = false;
        Chapter chapter = null;
        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);
        if (user != null) {
            chapter = historyService.findChapterReadByUser(user.getId(), storyId);
            checkConverter = Objects.equals(user.getId(), storySummary.getUserId());
            //N???u ng?????i ?????c l?? ng?????i ????ng th?? t??nh l?? ???? ????nh gi??
            if (storySummary.getUserId().equals(user.getId())) {
                // Ng?????i ?????c l?? Converter
                checkRating = true;
            } else {

                // Ki???m tra Ng?????i d??ng ???? ????nh gi?? ch??a
                if (userRatingService.existsRatingWithUser(storyId, user.getId())) {
                    // Ng?????i d??ng ???? ????nh gi??
                    checkRating = true;
                }
            }
        }

        checkStoryDetail.setRating(checkRating);
        checkStoryDetail.setCheckConverter(checkConverter);
        checkStoryDetail.setReadChapter(chapter);

        return new ResponseEntity<>(checkStoryDetail, HttpStatus.OK);

    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}
