package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.response.ChapterSummary;
import com.cyber.online_books.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Long > {

    /**
     *
     * @param userId
     * @param storyId
     * @param pageable
     * @return
     */
    Page<ChapterOfStory> findByUser_IdAndStory_Id(Long userId, Long storyId, Pageable pageable);

    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @param pageable
     * @return Page<ChapterOfStory>
     */
    Page< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus,
                                                                      Pageable pageable);

    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    List< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus);

    /**
     * Lấy Chapter Đầu Tiên của truyện
     *
     * @param storyId
     * @param listStatus
     * @return Optional<ChapterSummary>
     */
    @Query(value = ConstantsQueryUtils.CHAPTER_HEAD,
            nativeQuery = true)
    Optional<ChapterSummary> findChapterHead(@Param("storyId") Long storyId,
                                             @Param("chapterStatus") List< Integer > listStatus);

    /**
     * Lấy Chapter Mới Nhất của truyện
     *
     * @param storyId
     * @param chapterStatus
     * @return Optional<ChapterSummary>
     */
    @Query(value = ConstantsQueryUtils.CHAPTER_NEW,
            nativeQuery = true)
    Optional< ChapterSummary > findChapterNew(@Param("storyId") Long storyId,
                                              @Param("chapterStatus") List< Integer > chapterStatus);

    /**
     * Lấy Chapter theo
     *
     * @param storyId
     * @param listStatusStory
     * @param chapterId
     * @param listStatusChapter
     * @return Optional<Chapter>
     */
    Optional< Chapter > findByStory_IdAndStory_StatusInAndIdAndStatusIn(Long storyId, List< Integer > listStatusStory,
                                                                        Long chapterId, List< Integer > listStatusChapter);
}
