package com.cyber.online_books.controller.account;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.ChapterResponse;
import com.cyber.online_books.service.*;
import com.cyber.online_books.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/tai-khoan/chapter")
public class AccountChapterController extends ExceptionHandling {

    private final StoryService storyService;
    private final UserService userService;
    private final ChapterService chapterService;
    private final PayService payService;
    private final HistoryService historyService;

    @Autowired
    public AccountChapterController(StoryService storyService, UserService userService, ChapterService chapterService, PayService payService, HistoryService historyService) {
        this.storyService = storyService;
        this.userService = userService;
        this.chapterService = chapterService;
        this.payService = payService;
        this.historyService = historyService;
    }

    @GetMapping("/{sID}/{chID}")
    public ResponseEntity< ? > chapterPage(@PathVariable("sID") Long sid,
                                           @PathVariable("chID") Long chid,
                                           Principal principal,
                                           HttpServletRequest request) throws Exception {

        ChapterResponse chapterResponse = new ChapterResponse();

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

        //Lấy Chapter Theo sID và chID
        Chapter chapter = chapterService.findChapterByStoryIdAndChapterID(sid, ConstantsListUtils.LIST_STORY_DISPLAY,
                chid, ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        Long preChapter = chapterService.findPreviousChapterID(chapter.getSerial(), chapter.getStory().getId());
        Long nextChapter = chapterService.findNextChapterID(chapter.getSerial(), chapter.getStory().getId());

        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();

        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);

        // Lấy thời gian 30 phút trước
        Date halfHourAgo = DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR);

        //Lấy LocationIP Client
        String locationIP = getLocationIP(request);

        boolean checkVip = checkVipStory(user, chapter, dayAgo, now);

        //Lưu Lịch Sử Đọc Truyện
        saveFavorites(user, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }

        chapterResponse.setChapter(chapter);
        chapterResponse.setPreChapter(preChapter);
        chapterResponse.setNextChapter(nextChapter);
        chapterResponse.setCheckVip(checkVip);

        return new ResponseEntity<>(chapterResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/chapter-nguoi-dung")
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
        String content = chapter.getContent().replaceAll("\n", "\r\n");
        chapter.setContent(content.replaceAll("\n", "<br />"));

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

    private boolean checkVipStory(User user,
                                  Chapter chapter,
                                  Date dayAgo,
                                  Date now) {
        boolean check = true;

        //Kiểm Tra Chapter có phải tính phí hay không
        //Chapter tính phí là chapter có chStatus = 2
        if (chapter.getStatus() == 2) {

            // Kiểm tra người dùng đã đăng nhập chưa
            if (user != null) {
                //Kiểm tra người dùng có phải người đăng chapter không
                boolean checkUser = user.equals(chapter.getUser());
                // Kiểm tra người dùng đã thanh toán chương vip trong 24h qua không
                // Nếu chưa thanh toán rồi thì check = false
                if (!checkUser) {
                    boolean checkPay = checkDealStory(chapter.getId(), user.getId(), dayAgo, now);
                    if (!checkPay) {
                        check = false;
                    }
                }
            } else {
                check = false;
            }
        }
        return check;
    }

    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";

        //Kiểm Tra HttpServletRequest có null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

    private boolean checkDealStory(Long chID,
                                   Long uID,
                                   Date dayAgo,
                                   Date now) {
        boolean check = payService.checkDealChapterVip(chID, uID, dayAgo, now);
        return check;
    }

    private void saveFavorites(User user,
                               Chapter chapter,
                               Date halfHourAgo,
                               Date now,
                               String LocationIP) throws Exception {
        // Kiểm Tra đã đọc Chapter trong Khoảng
        boolean check = historyService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;

        if (check) {
            uView = 0;
        } else {
            // Chưa Đọc Chapter Trong Khoảng 30 phút Thì Tăng Lượt View Của Chapter
            chapterService.updateViewChapter(chapter);
        }
        historyService.saveHistory(chapter, user, LocationIP, uView);
    }

}
