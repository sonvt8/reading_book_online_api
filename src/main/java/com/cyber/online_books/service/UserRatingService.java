package com.cyber.online_books.service;

import com.cyber.online_books.entity.UserRating;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */
public interface UserRatingService {
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    boolean existsRatingWithUser(Long storyId, Long userId);
    
    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param storyId
     * @return Long
     */
    Long countRatingStory(Long storyId);
    
    /**
     * Tìm UserRating theo
     *
     * @param storyId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return UserRating
     */
    UserRating existsRatingWithLocationIP(Long storyId, String locationIP, Date startDate, Date endDate);
    
    /**
     * Thực Hiện Đánh giá
     *
     * @param uID
     * @param sID
     * @param locationIP
     * @param rating
     * @return true / false
     */
    Float saveRating(Long uID, Long sID, String locationIP, Integer rating);
    
    
}
