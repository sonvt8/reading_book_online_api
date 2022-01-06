package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;

public interface HistoryService {

    /**
     * Lấy Chapter Mới đọc
     *
     * @param userId
     * @param storyId
     * @return Chapter
     */
    Chapter findChapterReadByUser(Long userId, Long storyId);
}
