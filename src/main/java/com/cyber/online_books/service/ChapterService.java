package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.response.ChapterSummary;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChapterService {

    Chapter saveNewChapter(Chapter chapter, Long id);

    Chapter updateChapter(Chapter chapter, Long id);

    /**
     *
     * @param id
     * @return
     */
    Chapter findChapterById(Long id);

    boolean deleteChapter(Long id);

    /**
     * Tìm Kiếm Chapter theo
     * @param storyId
     * @param userId
     * @param type
     * @return
     */
    Page<ChapterOfStory> findByStoryIdAndUserId(Long storyId, Long userId, Integer type, Integer pagenumber);

    /**
     * Lấy danh sách Chapter của Truyện Theo
     *
     * @param storyId
     * @param pagenumber
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    Page< ChapterOfStory > getListChapterOfStory(Long storyId,
                                                 Integer pagenumber,
                                                 List< Integer > listChapterStatus,
                                                 Integer type);

    /**
     * Tìm Chapter Đầu Tiên Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    ChapterSummary findChapterHeadOfStory(Long storyId,
                                          List< Integer > listStatus);

    /**
     * Tìm Chapter Mới Nhất Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    ChapterSummary findChapterNewOfStory(Long storyId,
                                         List< Integer > listStatus);

    /**
     * Tìm Chapter Theo Story ID và Chapter ID
     *
     * @param storyId
     * @param listStatusStory
     * @param chapterId
     * @param listStatusChapter
     * @return Chapter
     * @throws Exception
     */
    Chapter findChapterByStoryIdAndChapterID(Long storyId, List< Integer > listStatusStory,
                                             Long chapterId, List< Integer > listStatusChapter) throws Exception;

    /**
     * Cập Nhật Lượt Xem Của Chapter
     *
     * @param chapter
     * @throws Exception
     */
    void updateViewChapter(Chapter chapter) throws Exception;

    /**
     * Lấy Chapter ID Trước
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    Long findPreviousChapterID(Float serial, Long storyId);

    /**
     * Lấy Chapter ID Tiếp Theo
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    Long findNextChapterID(Float serial, Long storyId);
}
