package com.cyber.online_books.controller.account;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Category;
import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.DateUtils;
import com.cyber.online_books.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/tai-khoan/chuong")
public class AccountChapterController {

    private final StoryService storyService;
    private final UserService userService;
    private final ChapterService chapterService;

    @Autowired
    public AccountChapterController(StoryService storyService, UserService userService, ChapterService chapterService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
    }

    @GetMapping(value = "/chapterOfUser")
    public ResponseEntity< ? > loadChapterOfStoryWithUser(@RequestParam("storyId") String storyId,
                                                          @RequestParam("pagenumber") Integer pagenumber,
                                                          @RequestParam("type") Integer type, Principal principal) throws Exception {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        User user = userService.findUserAccount(principal.getName());

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        if (storyId == null || WebUtils.checkLongNumber(storyId)) {
            throw new HttpMyException("Có lỗi xảy ra! Mong bạn quay lại sau.");
        }
        return new ResponseEntity<>(chapterService
                .findByStoryIdAndUserId(Long.parseLong(storyId), user.getId(), type, pagenumber),
                HttpStatus.OK);
    }

    @PostMapping("/them/{id}")
    public ResponseEntity<Chapter> addChapter(@RequestBody Chapter chapter, @PathVariable("id") Long id, Principal principal) throws UserNotLoginException, HttpMyException, UserNotFoundException {

        Story story = storyService.findStoryById(id);
        if(story == null)
            throw new HttpMyException("truyện không tồn tại");
        chapter.setStory(story);

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        chapter.setUser(user);

        if (story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            throw new HttpMyException("Truyện đã bị khóa không thể đăng thêm chương");
        } else {
            if (story.getDealStatus().equals(1)) {
                chapter.setStatus(ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
                chapter.setPrice(story.getPrice());
                chapter.setDealine(DateUtils.getDateDeal(story.getTimeDeal()));
                return new ResponseEntity<>(chapterService.saveNewChapter(chapter, id), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(chapterService.saveNewChapter(chapter, id), HttpStatus.OK);
    }

    @PostMapping("/sua-chuong/{id}")
    public ResponseEntity<Chapter> updateChapter(@RequestBody Chapter chapter, @PathVariable("id") Long id, Principal principal) throws UserNotLoginException, HttpMyException, UserNotFoundException {

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("Tài khoản không tồn tại");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        Chapter chapterEdit = chapterService.findChapterById(id);
        if (chapterEdit == null){
            throw new HttpMyException("chương không tồn tại");
        }
        if (!chapterEdit.getUser().getId().equals(user.getId())){
            throw new HttpMyException("Bạn không có quyền sửa chương không do bạn đăng!");
        }

        if (chapterEdit.getStory().getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)){
            throw new HttpMyException("Bạn không có quyền sửa Chapter thuộc Truyện bị khóa!");
        }
        if (chapterEdit.getStatus().equals(ConstantsStatusUtils.CHAPTER_DENIED) && chapterEdit.getStatus().equals(chapter.getStatus())){
            throw new HttpMyException("Bạn không có quyền Cập Nhật Trạng Thái Chapter bị khóa!");
        }
        chapter.setContent(chapter.getContent().replaceAll("\n", "<br />"));

        return new ResponseEntity<>(chapterService.updateChapter(chapter, id), HttpStatus.OK);
    }

    @DeleteMapping("/xoa-chuong/{id}")
    public ResponseEntity<HttpResponse> deleteChapter(@PathVariable("id") Long id, Principal principal) throws HttpMyException, UserNotLoginException {
        Chapter chapter = chapterService.findChapterById(id);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userService.findUserAccount(currentUsername);

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        if(chapter == null)
            throw new HttpMyException("không tìm thấy chương để xóa");
        boolean result = chapterService.deleteChapter(id);
        if(result)
            return response(HttpStatus.OK, "chương xóa thành công");
        else
            throw new HttpMyException("không thể xóa chương này");
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

}
