package com.cyber.online_books.repository;

import com.cyber.online_books.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long > {
    /**
     * Lấy Lịch sử đọc mới nhất
     *
     * @param userId
     * @param storyId
     * @return Optional<History>
     */
    Optional< History > findTopByUser_IdAndChapter_Story_IdAndChapter_StatusInOrderByDateViewDesc(Long userId, Long storyId, List< Integer > status);

    /**
     * Kiểm tra tồn tại Favorites trong khoảng
     *
     * @param chapterId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return boolean
     */
    boolean existsByChapter_IdAndLocationIPAndDateViewBetween(Long chapterId, String locationIP, Date startDate, Date endDate);
}
