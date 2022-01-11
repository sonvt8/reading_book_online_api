package com.cyber.online_books.service;

import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.*;
import com.cyber.online_books.response.TopConverter;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {

    /**
     * Lấy toàn bộ User
     *
     * @return List<User>
     */
    Page< User > getUsers(int page, int size);

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
     * @param page
     * @param size
     * @return
     */
    List<TopConverter> findTopConverter(int page, int size);

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
     * @param newNick
     */
    User updateDisplayName(Principal principal, String newNick) throws HttpMyException, UserNotFoundException;

    /**
     * Cập nhật thông báo
     *
     * @param newMess
     */
    User updateNotification(Principal principal, String newMess) throws UserNotFoundException;

    /**
     * Cập nhật thông báo
     *
     * @param sourceFile
     */
    User updateAvatar(Principal principal, MultipartFile sourceFile) throws UserNotFoundException, NotAnImageFileException;

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
    void updatePassword(String oldPassword,String newPassword, Principal principal) throws UserNotFoundException, HttpMyException;

    /**
     * Cập Nhật User
     *
     * @param user
     * @return User
     */
    User updateUser(User user) throws UserNotFoundException, UsernameExistException, EmailExistException;

    /**
     * Nạp đậu cho User
     *
     * @param money
     * @param id
     */
    void topUp(Double money, Long id, Principal principal) throws UserNotFoundException, HttpMyException;

    /**
     * Xoá User
     *
     * @param id
     */
    void deleteUser(Principal principal, Long id) throws HttpMyException, IOException, UserNotFoundException;
}
