package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.response.ChapterOfStory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChapterService {

    Chapter saveNewChapter(Chapter chapter, Long id);

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
}
