package com.cyber.online_books.service;

import com.cyber.online_books.entity.UserFollow;
import com.cyber.online_books.response.FollowSummary;
import org.springframework.data.domain.Page;

public interface UserFollowService {

    /**
     * @param id
     * @param pagenumber
     * @return Page< FollowSummar >
     */
    Page<FollowSummary> findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size);

    /**
     * @param userId
     * @param storyId
     * @return UserFollow
     */
    UserFollow findByUserIdAndStoryId(Long userId, Long storyId);

    /**
     * @param userFollow
     */
    void deleteFollow(UserFollow userFollow);

    /**
     *
     * @param userFollow
     */
    void saveFollow(UserFollow userFollow);

    /**
     * @param userId - Id của người dùng
     * @param storyId- Id của Truyện
     * @return {@code true} nếu tồn tại, nếu không tồn tại thì {@code false}
     */
    boolean existsUserFollow(Long userId, Long storyId);
}
