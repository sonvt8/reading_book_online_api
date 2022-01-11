package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.UserRating;
import com.cyber.online_books.repository.UserRatingRepository;
import com.cyber.online_books.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
public class UserRatingServiceImpl implements UserRatingService {
    
    private final UserRatingRepository userRatingRepository;
    
    @Autowired
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository) {
        this.userRatingRepository = userRatingRepository;
    }
    
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    @Override
    public boolean existsRatingWithUser(Long storyId, Long userId) {
        return userRatingRepository.existsSratingByStory_IdAndUser_Id(storyId, userId);
    }
    
    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param storyId
     * @return Long
     */
    @Override
    public Long countRatingStory(Long storyId) {
        return userRatingRepository
                .countByStory_Id(storyId);
    }
    
    /**
     * Tìm UserRating theo
     *
     * @param storyId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return UserRating
     */
    @Override
    public UserRating existsRatingWithLocationIP(Long storyId, String locationIP, Date startDate, Date endDate) {
        return userRatingRepository
                .findByStory_IdAndLocationIPAndCreateDateBetween(storyId, locationIP, startDate, endDate)
                .orElse(null);
    }
    
    /**
     * Thực Hiện Đánh giá
     *
     * @param uID
     * @param sID
     * @param locationIP
     * @param rating
     * @return true / false
     */
    @Override
    public Float saveRating(Long uID, Long sID, String locationIP, Integer rating) {
        return userRatingRepository
                .saveRating(uID,sID,locationIP,rating);
    }
    
}
