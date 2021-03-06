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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(AccountChapterController.class);
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
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        //L???y Chapter Theo sID v?? chID
        Chapter chapter = chapterService.findChapterByStoryIdAndChapterID(sid, ConstantsListUtils.LIST_STORY_DISPLAY,
                chid, ConstantsListUtils.LIST_CHAPTER_DISPLAY);

        Long preChapter = chapterService.findPreviousChapterID(chapter.getSerial(), chapter.getStory().getId());
        Long nextChapter = chapterService.findNextChapterID(chapter.getSerial(), chapter.getStory().getId());

        //L???y Th???i Gian Hi???n T???i
        Date now = DateUtils.getCurrentDate();

        // L???y Th???i Gian 24h Tr?????c
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);

        // L???y th???i gian 30 ph??t tr?????c
        Date halfHourAgo = DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR);

        //L???y LocationIP Client
        String locationIP = getLocationIP(request);

        boolean checkVip = checkVipStory(user, chapter, dayAgo, now);

        //L??u L???ch S??? ?????c Truy???n
        saveFavorites(user, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }

        chapterResponse.setChapter(chapter);
        chapterResponse.setPreChapter(preChapter);
        chapterResponse.setNextChapter(nextChapter);
        chapterResponse.setCheckVip(checkVip);
        chapterResponse.setTimeDealDay(DateUtils.betweenTwoDays2(chapter.getDealine()));

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
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        if (storyId == null || WebUtils.checkLongNumber(storyId)) {
            throw new HttpMyException("C?? l???i x???y ra! Mong b???n quay l???i sau.");
        }
        return new ResponseEntity<>(chapterService
                .findByStoryIdAndUserId(Long.parseLong(storyId), user.getId(), type, pagenumber),
                HttpStatus.OK);
    }

    @PostMapping("/them/{id}")
    public ResponseEntity<Chapter> addChapter(@RequestBody Chapter chapter, @PathVariable("id") Long id, Principal principal) throws UserNotLoginException, HttpMyException, UserNotFoundException {

        Story story = storyService.findStoryById(id);
        if(story == null)
            throw new HttpMyException("truy???n kh??ng t???n t???i");
        chapter.setStory(story);

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();

        User user = userService.findUserAccount(currentUsername);

        if (user == null) {
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        chapter.setUser(user);

        if (story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)) {
            throw new HttpMyException("Truy???n ???? b??? kh??a kh??ng th??? ????ng th??m ch????ng");
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
            throw new UserNotFoundException("T??i kho???n kh??ng t???n t???i");
        }

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        Chapter chapterEdit = chapterService.findChapterById(id);
        if (chapterEdit == null){
            throw new HttpMyException("ch????ng kh??ng t???n t???i");
        }
        if (!chapterEdit.getUser().getId().equals(user.getId())){
            throw new HttpMyException("B???n kh??ng c?? quy???n s???a ch????ng kh??ng do b???n ????ng!");
        }

        if (chapterEdit.getStory().getStatus().equals(ConstantsStatusUtils.STORY_STATUS_HIDDEN)){
            throw new HttpMyException("B???n kh??ng c?? quy???n s???a Chapter thu???c Truy???n b??? kh??a!");
        }
        if (chapterEdit.getStatus().equals(ConstantsStatusUtils.CHAPTER_DENIED) && chapterEdit.getStatus().equals(chapter.getStatus())){
            throw new HttpMyException("B???n kh??ng c?? quy???n C???p Nh???t Tr???ng Th??i Chapter b??? kh??a!");
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
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        if(chapter == null)
            throw new HttpMyException("kh??ng t??m th???y ch????ng ????? x??a");
        boolean result = chapterService.deleteChapter(id);
        if(result)
            return response(HttpStatus.OK, "ch????ng x??a th??nh c??ng");
        else
            throw new HttpMyException("kh??ng th??? x??a ch????ng n??y");
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

        //Ki???m Tra Chapter c?? ph???i t??nh ph?? hay kh??ng
        //Chapter t??nh ph?? l?? chapter c?? chStatus = 2
        if (chapter.getStatus() == 2) {
            if(DateUtils.betweenTwoDays2(chapter.getDealine())>0){
                check = true;
            }
            else {
                if (user != null) {

                    //Ki???m tra ng?????i d??ng c?? ph???i ng?????i ????ng chapter kh??ng
                    boolean checkUser = user.equals(chapter.getUser());
                    // Ki???m tra ng?????i d??ng ???? thanh to??n ch????ng vip trong 24h qua kh??ng
                    // N???u ch??a thanh to??n r???i th?? check = false
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
            // Ki???m tra ng?????i d??ng ???? ????ng nh???p ch??a

        }
        return check;
    }

    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";

        //Ki???m Tra HttpServletRequest c?? null
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
        // Ki???m Tra ???? ?????c Chapter trong Kho???ng
        boolean check = historyService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;

        if (check) {
            uView = 0;
        } else {
            // Ch??a ?????c Chapter Trong Kho???ng 30 ph??t Th?? T??ng L?????t View C???a Chapter
            chapterService.updateViewChapter(chapter);
        }
        historyService.saveHistory(chapter, user, LocationIP, uView);
    }

}
