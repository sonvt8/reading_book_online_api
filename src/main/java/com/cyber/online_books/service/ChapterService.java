package com.cyber.online_books.service;

import com.cyber.online_books.response.ChapterOfStory;
import org.springframework.data.domain.Page;

public interface ChapterService {

    /**
     * Tìm Kiếm Chapter theo
     * @param storyId
     * @param userId
     * @param type
     * @return
     */
    Page<ChapterOfStory> findByStoryIdAndUserId(Long storyId, Long userId, Integer type, Integer pagenumber);
}
