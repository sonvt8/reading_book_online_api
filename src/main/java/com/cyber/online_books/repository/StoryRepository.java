package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.response.*;
import com.cyber.online_books.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long > {
    Story findStoryByName(String name);

    /**
     * Lấy danh sách Truyện theo Id người đăng và Status
     *
     * @param id
     * @param status
     * @param pageable
     * @return
     */
    @Query("SELECT s FROM Story s WHERE s.user.id = ?1 and s.status = ?2")
    Page<StoryUser> findByUser_IdAndStatusOrderByUpdateDateDesc(Long id, Integer status, Pageable pageable);

    /**
     * Lấy Danh sách Truyện Mới Theo Thể Loại
     *
     * @param listChStatus
     * @param cID
     * @param listStatus
     * @param pageable
     * @return Page<StoryUpdate>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_CATEGORY,
            nativeQuery = true)
    Page<StoryUpdate> findStoryNewByCategory(@Param("categoryId") Integer cID,
                                             @Param("storyStatus") List< Integer > listStatus,
                                             @Param("chapterStatus") List< Integer > listChStatus,
                                             Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top  View Theo Category
     *
     * @param cID
     * @param historyStatus
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW_BY_CATEGORY,
            nativeQuery = true)
    Page<StoryTop> findTopViewByCategory(@Param("categoryID") Integer cID,
                                         @Param("historyStatus") Integer historyStatus,
                                         @Param("storyStatus") List< Integer > listStatus,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate, Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top  Đề Cử Theo Category
     *
     * @param cID
     * @param listStatus
     * @param payType
     * @param payStatus
     * @param startDate
     * @param endDate
     * @param pageable
     * @return Page<StoryTop>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VOTE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VOTE_BY_CATEGORY,
            nativeQuery = true)
    Page< StoryTop > findTopVoteByCategory(@Param("categoryID") Integer cID,
                                           @Param("storyStatus") List< Integer > listStatus,
                                           @Param("payType") Integer payType, @Param("payStatus") Integer payStatus,
                                           @Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                           Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top
     *
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param historyStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW_BY_STATUS,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW_BY_STATUS,
            nativeQuery = true)
    Page< StoryTop > findStoryTopViewByStatus(@Param("storyStatus") List< Integer > listStatus,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("historyStatus") Integer historyStatus,
                                              Pageable pageable);

    /**
     * Lấy Danh sách Truyện Vip mới cập nhật
     *
     * @param listChapterStatus - danh sách trạng thái chapter
     * @param pageable          - biến page
     * @param listStoryStatus   - danh sách trạng thái truyện
     * @param sDealStatus       - trạng thái truyện trả tiền
     * @return Page<StoryUpdate>
     */
    @Query(value = ConstantsQueryUtils.VIP_STORY_NEW_UPDATE,
            countQuery = ConstantsQueryUtils.COUNT_VIP_STORY_NEW_UPDATE,
            nativeQuery = true)
    Page< StoryUpdate > findVipStoryNew(@Param("chapterStatus") List< Integer > listChapterStatus,
                                        @Param("storyStatus") List< Integer > listStoryStatus,
                                        @Param("storyDealStatus") Integer sDealStatus,
                                        Pageable pageable);

    /**
     * Lấy Danh sách Truyện Mới Cập Nhật Theo Status
     *
     * @param listChapterStatus - Danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái Story
     * @param pageable          - biến phân trang
     * @return Page<NewStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_STATUS,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_STATUS,
            nativeQuery = true)
    Page< StoryUpdate > getPageStoryComplete(@Param("chapterStatus") List< Integer > listChapterStatus,
                                             @Param("storyStatus") List< Integer > listStoryStatus,
                                             Pageable pageable);

    /**
     * Lấy Danh sách Truyện Top Đề Cử
     *
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param payType
     * @param payStatus
     * @param pageable
     * @return
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_APPOIND,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_APPOIND,
            nativeQuery = true)
    Page< StoryTop > findTopStoryAppoind(@Param("storyStatus") List< Integer > listStatus,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("payType") Integer payType, @Param("payStatus") Integer payStatus,
                                         Pageable pageable);

    /**
     * Tìm truyện theo id và status thỏa mãn
     *
     * @param storyId
     * @param listStoryStatus
     * @return Optional<StorySummary>
     */
    Optional<StorySummary> findByIdAndStatusIn(Long storyId, List< Integer > listStoryStatus);

    /**
     * Lấy Danh sách Top 5 truyện mới đăng theo user và status
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    List< StorySlide > findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(Long userId, List< Integer > listStoryDisplay);

    /**
     * Tìm truyện theo id và status thỏa mãn
     *
     * @param storyId
     * @param listStoryStatus
     * @return Optional<Story>
     */
    Optional< Story > findStoryByIdAndStatusIn(Long storyId, List< Integer > listStoryStatus);

    /**
     * Lấy Page truyện theo id và status
     *
     * @param userId
     * @param listStatus
     * @param pageable
     * @return
     */
    Page< StoryMember > findByUser_IdAndStatusInOrderByCreateDateDesc(Long userId,
                                                                      List< Integer > listStatus,
                                                                      Pageable pageable);

    /**
     * Lấy danh sách truyện theo user_id và status
     *
     * @param userId
     * @param listStatus
     * @return
     */
    List< StoryMember > findAllByUser_IdAndStatusInOrderByCreateDateDesc(Long userId,
                                                                         List< Integer > listStatus);

    Page<StoryAdmin> findByOrderByIdDesc(Pageable pageable);

    Page< StoryAdmin > findByNameContainingOrderByIdDesc(String search, Pageable pageable);

    Page< StoryAdmin > findByDealStatusOrderByIdDesc(Integer status, Pageable pageable);

    Page< StoryAdmin > findByDealStatusAndNameContainingOrderByIdDesc(Integer status, String search, Pageable pageable);

    Page< StoryAdmin > findByNameContainingAndStatusOrderByIdDesc(String search, Integer status, Pageable pageable);

    Page< StoryAdmin > findByStatusOrderByIdDesc(Integer status, Pageable pageable);
}
