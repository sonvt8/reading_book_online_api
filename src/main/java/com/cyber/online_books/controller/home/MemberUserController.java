package com.cyber.online_books.controller.home;

import com.cyber.online_books.domain.HttpResponse;
import com.cyber.online_books.domain.UserPrincipal;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.ExceptionHandling;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.response.CommentSummary;
import com.cyber.online_books.response.ConveterSummary;
import com.cyber.online_books.response.TopConverter;
import com.cyber.online_books.service.CommentService;
import com.cyber.online_books.service.UserService;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cyber.online_books.utils.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path= {"/thanh_vien"})
public class MemberUserController extends ExceptionHandling {
    public static final String EMAIL_SENT = "Mật khẩu mới đã được gửi đến email: ";
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;
    private final CommentService commentService;

    @Autowired
    public MemberUserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider, CommentService commentService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.commentService = commentService;
    }

    @PostMapping("/dang_ky")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException, HttpMyException {
        User newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/dang_nhap")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserAccount(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @GetMapping("/xem_top_converter")
    public ResponseEntity< ? > loadStoryOfConverter() {
        // Lấy Danh Sách Top Converter
        List<TopConverter> topConverters = userService
                .findTopConverter(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE);

        return new ResponseEntity<>(topConverters, HttpStatus.OK);
    }

    @PostMapping(value = "/thong-tin-converter")
    public ResponseEntity< ? > loadConverter(@RequestParam("userId") Long userId) {
        ConveterSummary conveterSummary = userService.findConverterByID(userId);
        return new ResponseEntity<>(conveterSummary, HttpStatus.OK);
    }

    @PostMapping("/quen_mat_khau")
    @ResponseBody
    public ResponseEntity<HttpResponse> resetPassword(@RequestParam("email") String email) throws HttpMyException, EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

    @PostMapping(value = "/binh-luan/xem")
    public ResponseEntity<Page<CommentSummary>> loadCommentOfStory(@RequestParam("storyId") Long storyId,
                                                                   @RequestParam("pagenumber") Integer pagenumber,
                                                                   @RequestParam("type") Integer type) {
        Page<CommentSummary> commentSummaryPage = commentService.getListCommentOfStory(storyId, pagenumber, type);
        return new ResponseEntity<>(commentSummaryPage, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }
}
