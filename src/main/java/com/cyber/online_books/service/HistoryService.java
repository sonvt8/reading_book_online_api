package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.User;

import java.util.Date;

public interface HistoryService {

    /**
     * Lấy Chapter Mới đọc
     *
     * @param userId
     * @param storyId
     * @return Chapter
     */
    Chapter findChapterReadByUser(Long userId, Long storyId);

    /**
     * Kiểm tra tồn tại Favorites trong khoảng
     *
     * @param chapterId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return boolean
     */
    boolean checkChapterAndLocationIPInTime(Long chapterId, String locationIP, Date startDate, Date endDate);

    /**
     * Lưu Lịch Sử đọc truyện
     *
     * @param chapter
     * @param user
     * @param LocationIP
     * @param uView
     * @return void
     */
    void saveHistory(Chapter chapter, User user, String LocationIP, Integer uView);
}
