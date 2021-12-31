package com.cyber.online_books.service;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.StoryAdmin;
import com.cyber.online_books.response.StoryTop;
import com.cyber.online_books.response.StoryUpdate;
import com.cyber.online_books.response.StoryUser;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;

public interface StoryService {
    Story findStoryById(Long id);

    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param cID
     * @param page
     * @param size
     * @param chapterStatus
     * @param storyStatus
     * @return Page<StoryUpdate>
     */
    Page<StoryUpdate> findStoryNewUpdateByCategoryId(Integer cID,
                                                     int page, int size,
                                                     List< Integer > storyStatus, List< Integer > chapterStatus);

    /**
     * Lấy List Truyện đăng bởi User
     *
     * @param id         - id của User đăng
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @param status     - Trạng Thái Truyện
     * @return
     */
    Page<StoryUser> findPageStoryByUser(Long id, int pagenumber, Integer size, Integer status);

    /**
     * Lấy List Truyện Top View theo Category
     *
     * @param categoryId
     * @param historyStatus
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    Page<StoryTop> findStoryTopViewByCategoryId(Integer categoryId, Integer historyStatus,
                                                List< Integer > listStatus,
                                                Date startDate, Date endDate,
                                                int page, int size);

    /**
     * Lấy Danh sách Truyện Top  Đề Cử Theo Category
     *
     * @param categoryID
     * @param storyStatus
     * @param payType
     * @param payStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    Page< StoryTop > findStoryTopVoteByCategoryId(Integer categoryID, List< Integer > storyStatus,
                                                  Integer payType, Integer payStatus,
                                                  Date startDate, Date endDate,
                                                  int page, int size);

    Page<StoryAdmin> findStoryInAdmin(Integer pagenumber, Integer size, Integer type, String search);

    boolean deleteStory(Long id);

    Story addNewStory(String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws UserNotLoginException, NotAnImageFileException, HttpMyException, HttpMyException;

    Story updateAccountStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException;

    Story updateAdminStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Double price, Integer timeDeal, Integer dealStatus, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException;
}
