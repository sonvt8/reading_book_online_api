package com.cyber.online_books.controller.account;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.entity.UserRating;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.RatingResponse;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.service.UserRatingService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.DateUtils;
import com.cyber.online_books.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Date;

import static com.cyber.online_books.utils.UserImplContant.NO_USER_FOUND_BY_USERNAME;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/tai_khoan/danh_gia")
public class AccountRatingController {
    @Autowired
    private UserRatingService userRatingService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;

    @PostMapping(value = "")
    @Transactional
    public ResponseEntity<RatingResponse> saveUserRating(@RequestParam("idBox") Long idBox,
                                                   @RequestParam("rate") String rate,
                                                   HttpServletRequest request,
                                                   Principal principal) throws Exception {
        User currentUser = validatePricipal(principal);
        Story story = storyService.findStoryByIdAndStatus(idBox, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            throw new HttpMyException("kh??ng t??m th???y truy???n");
        }
        if (userRatingService.existsRatingWithUser(idBox, currentUser.getId())) {
            throw new HttpMyException("B???n ???? ????nh gi?? truy???n n??y r???i");
        }
//        String locationIP = WebUtils.getLocationIP(request);
        String locationIP = WebUtils.getClientIp(request);
        Date now = DateUtils.getCurrentDate();
        UserRating userRating = userRatingService.existsRatingWithLocationIP(idBox, locationIP,
                DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR), now);//l???y userRating ???? ????nh gi?? truy???n trong kho???ng 30 ph??t tr?????c theo IP
        if (userRating != null) {
            throw new HttpMyException("???? c?? ????nh gi?? truy???n t???i ?????a ch??? IP n??y. H??y ?????i th??m " + DateUtils.betweenHours(userRating.getCreateDate()) + " ????? ti???p t???c ????nh gi??");
        }
        int rating = Integer.parseInt(rate);
        Float result = userRatingService.saveRating(currentUser.getId(), idBox, locationIP,rating);
        //L??u ????nh gi??
        if (result != -1) {
            RatingResponse ratingResponse = new RatingResponse();
            ratingResponse.setMyrating(userRatingService.countRatingStory(idBox));
            DecimalFormat df = new DecimalFormat("#.0");
            ratingResponse.setMyrate(df.format(result));
            return new ResponseEntity<>(ratingResponse, OK);
        }
        throw new HttpMyException("C?? l???i x???y ra khi th???c hi???n ????nh gi??");
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
