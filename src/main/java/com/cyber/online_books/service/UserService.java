package com.cyber.online_books.service;

import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;

import javax.mail.MessagingException;
import java.security.Principal;

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
     * Tìm user theo Id
     *
     * @param id
     * @return User - nếu tồn tại / null- nếu không tồn tại user
     */
    User findUserById(Long id);

    /**
     * Kiểm tra DisplayName đã tồn tại chưa
     *
     * @param userId
     * @param newNick
     * @return boolean
     */
    boolean checkUserDisplayNameExits(Long userId, String newNick);

    /**
     * Cập nhật ngoại hiệu
     *
     * @param userId
     * @param money
     * @param newNick
     */
    void updateDisplayName(Long userId, Double money, String newNick) throws Exception;

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

    /**
     * Cập nhật mật khẩu
     *
     * @param newPassword
     */
    void updatePassword(String newPassword, Principal principal) throws HttpMyException;

    /**
     * Cập Nhật User
     *
     * @param user
     * @return User
     */
    User updateUser(User user);

    /**
     * Xoá User
     *
     * @param user
     */
    void deleteUser(User user);
}
