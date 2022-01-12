package com.cyber.online_books.controller.home;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.response.CommentSummary;
import com.cyber.online_books.service.CommentService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.cyber.online_books.utils.UserImplContant.NO_USER_FOUND_BY_USERNAME;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/binh_luan")
public class CommentController {
    private final CommentService commentService;

    private final UserService userService;

    private final StoryService storyService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, StoryService storyService) {
        this.commentService = commentService;
        this.userService = userService;
        this.storyService = storyService;
    }

    @PostMapping(value = "/xem")
    public ResponseEntity<Page<CommentSummary>> loadCommentOfStory(@RequestParam("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        Page<CommentSummary> commentSummaryPage = commentService.getListCommentOfStory(storyId, pagenumber, type);
        return new ResponseEntity<>(commentSummaryPage, OK);
    }

    @PostMapping(value = "/them")
    public ResponseEntity< ? > newComment(@RequestParam("storyId") Long storyId,
                                          @RequestParam("commentText") String commentTextEncode,
                                          Principal principal) throws Exception {
        User currentUser = validatePricipal(principal);
        Story story = storyService.findStoryByIdAndStatus(storyId, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            throw new HttpMyException("Truyện không tồn tại hoặc đã bị xóa!");
        }
        boolean check = commentService.saveComment(currentUser, story, commentTextEncode);
        if (check) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("Có lỗi xảy ra!");
        }
    }

    private User validatePricipal(Principal principal) throws UserNotFoundException {
        String currentUsername = principal.getName();
        User currentUser = userService.findUserAccount(currentUsername);
        if(currentUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        }
        if (currentUser.getStatus() != 1){
            throw new DisabledException("Tài khoản đã bị đóng hoặc chưa được kích hoạt");
        }
        return currentUser;
    }
}
