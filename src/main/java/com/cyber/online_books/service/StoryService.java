package com.cyber.online_books.service;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.response.*;
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

    /**
     * Lấy danh sách truyên top view trong khoảng theo status
     *
     * @param listStatus    - Danh sách trạng thái Story
     * @param startDate     - Ngày Bắt đầu
     * @param endDate       - Ngày kết thúc
     * @param historyStatus - Status history
     * @param page          - số trang
     * @param size          - độ dài trang
     * @return
     */
    Page< StoryTop > findStoryTopViewByStatuss(List< Integer > listStatus,
                                               Date startDate, Date endDate, Integer historyStatus,
                                               int page, int size);

    /**
     * Lấy Danh sách Truyện Vip mới cập nhật
     *
     * @param listChapterStatus - danh sách trạng thái chapter
     * @param pagenumber        - biến số trang
     * @param size              - biến size
     * @param listStoryStatus   - danh sách trạng thái truyện
     * @param sDealStatus       - trạng thái truyện trả tiền
     * @return Page<StoryUpdate>
     */
    Page< StoryUpdate > findStoryVipUpdateByStatus(List< Integer > listChapterStatus, List< Integer > listStoryStatus,
                                                   Integer sDealStatus, int pagenumber, Integer size);

    /**
     * Lấy Page Truyện theo Status
     *
     * @param listChapterStatus -  danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái Story
     * @param page              - số trang
     * @param size              - độ dài trang
     * @return Page<StoryUpdate>
     */
    Page< StoryUpdate > findStoryUpdateByStatus(List< Integer > listChapterStatus,
                                                List< Integer > listStoryStatus,
                                                int page, int size);

    /**
     * Lấy List Truyện Top Đề cử Trong Khoảng
     *
     * @param page
     * @param size
     * @return Page<TopStory>
     */
    public Page< StoryTop > getTopStoryAppoind(int page, int size, Date startDate, Date endDate);

    /**
     * Tìm Truyện Theo StoryID và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return StorySummar - nếu tồn tại truyện thỏa mãn điều kiện
     * @throws Exception - nếu không tồn tại truyện thỏa mãn điều kiện
     */
    StorySummary findStoryByStoryIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception;

    /**
     * Lấy Danh sách truyện mới đăng của Converter
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    List< StorySlide > findStoryOfConverter(Long userId, List< Integer > listStoryDisplay);

    /**
     * Tìm Truyện Theo Id và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return Story - nếu tồn tại truyện thỏa mãn điều kiện
     */
    Story findStoryByIdAndStatus(Long storyId, List< Integer > listStoryStatus);

    /**
     * Lấy số lượng truyện đăng bởi User
     *
     * @param userId
     * @param listStoryDisplay
     * @return Long
     */
    Long countStoryByUser(Long userId, List< Integer > listStoryDisplay);

    /**
     * Lấy danh sách truyện đã đăng của User
     *
     * @param userId
     * @param pagenumber
     * @param type
     * @param size
     * @param listStatus
     * @return
     */
    Page< StoryMember > findStoryByUserId(Long userId, List< Integer > listStatus,
                                          int pagenumber, int type, Integer size);


    /**
     * Lấy List Truyện Theo searchText
     *
     * @param searchText
     * @param listStatus
     * @return
     */
    List< StorySlide > findListStoryBySearchKey(String searchText, List< Integer > listStatus);

    /**
     * @param date
     * @return
     */
    Long countNewStoryInDate(Date date);

    Page<StoryAdmin> findStoryInAdmin(Integer pagenumber, Integer size, Integer type, String search);

    boolean deleteStory(Long id);

    Story addNewStory(String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws UserNotLoginException, NotAnImageFileException, HttpMyException, HttpMyException;

    Story updateAccountStory(Long id, String name, String author, String infomation, String[] category, Integer status, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException;

    Story updateAdminStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Double price, Integer timeDeal, Integer dealStatus, Integer status, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException;
}
