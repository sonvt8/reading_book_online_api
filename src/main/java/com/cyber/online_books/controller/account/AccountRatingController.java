package com.cyber.online_books.controller.account;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.entity.UserRating;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotFoundException;
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
            throw new HttpMyException("không tìm thấy truyện");
        }
        if (userRatingService.existsRatingWithUser(idBox, currentUser.getId())) {
            throw new HttpMyException("Bạn đã đánh giá truyện này rồi");
        }
//        String locationIP = WebUtils.getLocationIP(request);
        String locationIP = WebUtils.getClientIp(request);
        Date now = DateUtils.getCurrentDate();
        UserRating userRating = userRatingService.existsRatingWithLocationIP(idBox, locationIP,
                DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR), now);//lấy userRating đã đánh giá truyện trong khoảng 30 phút trước theo IP
        if (userRating != null) {
            throw new HttpMyException("Đã có đánh giá truyện tại địa chỉ IP này. Hãy đợi thêm " + DateUtils.betweenHours(userRating.getCreateDate()) + " để tiếp tục đánh giá");
        }
        int rating = Integer.parseInt(rate);
        Float result = userRatingService.saveRating(currentUser.getId(), idBox, locationIP,rating);
        //Lưu đánh giá
        if (result != -1) {
            RatingResponse ratingResponse = new RatingResponse();
            ratingResponse.setMyrating(userRatingService.countRatingStory(idBox));
            DecimalFormat df = new DecimalFormat("#.0");
            ratingResponse.setMyrate(df.format(result));
            return new ResponseEntity<>(ratingResponse, OK);
        }
        throw new HttpMyException("Có lỗi xảy ra khi thực hiện đánh giá");
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
