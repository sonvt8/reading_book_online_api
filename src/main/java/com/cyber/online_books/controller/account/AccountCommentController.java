package com.cyber.online_books.controller.account;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
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
@RequestMapping(value = "tai_khoan/binh_luan")
public class AccountCommentController {
    private final CommentService commentService;

    private final UserService userService;

    private final StoryService storyService;

    @Autowired
    public AccountCommentController(CommentService commentService, UserService userService, StoryService storyService) {
        this.commentService = commentService;
        this.userService = userService;
        this.storyService = storyService;
    }


    @PostMapping(value = "/them")
    public ResponseEntity< ? > newComment(@RequestParam("storyId") Long storyId,
                                          @RequestParam("commentText") String commentTextEncode,
                                          Principal principal) throws Exception {
        User currentUser = validatePricipal(principal);
        Story story = storyService.findStoryByIdAndStatus(storyId, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            throw new HttpMyException("Truy???n kh??ng t???n t???i ho???c ???? b??? x??a!");
        }
        boolean check = commentService.saveComment(currentUser, story, commentTextEncode);
        if (check) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("C?? l???i x???y ra!");
        }
    }

    private User validatePricipal(Principal principal) throws UserNotFoundException, UserNotLoginException {
        if (principal == null) {
            throw new UserNotLoginException();
        }
        String currentUsername = principal.getName();
        User currentUser = userService.findUserAccount(currentUsername);
        if(currentUser == null) {
            throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME);
        }
        if (currentUser.getStatus() != 1){
            throw new DisabledException("T??i kho???n ???? b??? ????ng ho???c ch??a ???????c k??ch ho???t");
        }
        return currentUser;
    }
}
