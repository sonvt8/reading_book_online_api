package com.cyber.online_books.service;

import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;

import javax.mail.MessagingException;

public interface UserService {
    /**
     * Tìm kiếm User theo username
     *
     * @param userName
     * @return User - Nếu tồn tại user với userName / null - nếu không tồn tại user với userName
     */
    User findUserAccount(String userName);

    /**
     * Tìm kiếm User theo email
     *
     * @param email
     * @return User - Nếu tồn tại user với email / null - nếu không tồn tại user với email
     */
    User findUserEmail(String email);

    /**
     * Đăng ký người dùng mới
     *
     * @param user
     * @return true - nếu đăng ký thành công / false - nếu có lỗi xảy ra
     */
    User registerUser(User user) throws UserNotFoundException, UsernameExistException, EmailExistException, HttpMyException;

    /**
     * Quên mật khẩu
     *
     * @param email
     */
    void resetPassword(String email) throws HttpMyException, EmailNotFoundException;
}
