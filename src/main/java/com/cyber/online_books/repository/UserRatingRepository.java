package com.cyber.online_books.repository;

import com.cyber.online_books.entity.UserRating;
import com.cyber.online_books.entity.UserRatingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */
@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, UserRatingPK> {
    
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn/ "false" nếu chưa có bình chọn phù hợp
     */
    boolean existsSratingByStory_IdAndUser_Id(Long storyId, Long userId);
    
    /**
     * Đếm số đánh giá
     *
     * @param storyId
     * @return Long
     */
    Long countByStory_Id(Long storyId);
    
    /**
     * Tìm UserRating theo
     *
     * @param storyId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return
     */
    Optional< UserRating > findByStory_IdAndLocationIPAndCreateDateBetween(Long storyId,
                                                                           String locationIP,
                                                                           Date startDate, Date endDate);
    
    /**
     * Thực Hiện Đánh giá
     *
     * @param userID
     * @param storyID
     * @param myLocationIP
     * @param myRating
     * @return Float
     */
    @Procedure("saveRating")
    Float saveRating(@Param("userID") Long userID,
                     @Param("storyID") Long storyID,
                     @Param("myLocationIP") String myLocationIP,
                     @Param("myRating") Integer myRating);
    
    
}
