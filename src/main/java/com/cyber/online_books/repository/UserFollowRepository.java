package com.cyber.online_books.repository;

import com.cyber.online_books.entity.UserFollow;
import com.cyber.online_books.entity.UserFollowPK;
import com.cyber.online_books.response.FollowSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowPK> {

    /**
     * @param id
     * @return
     */
    Page<FollowSummary> findByUser_IdOrderByStory_UpdateDateDesc(Long id, Pageable pageable);

    /**
     * @param userId
     * @param storyId
     * @return
     */
    Optional<UserFollow> findByUser_IdAndStory_Id(Long userId, Long storyId);
}
