package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.response.StoryAdmin;
import com.cyber.online_books.response.StoryUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoryRepository extends JpaRepository<Story, Long > {
    Story findStoryByName(String name);

    /**
     * Lấy danh sách Truyện theo Id người đăng và Status
     *
     * @param id
     * @param status
     * @param pageable
     * @return
     */
    @Query("SELECT s FROM Story s WHERE s.user.id = ?1 and s.status = ?2")
    Page<StoryUser> findByUser_IdAndStatusOrderByUpdateDateDesc(Long id, Integer status, Pageable pageable);

    Page<StoryAdmin> findByOrderByIdDesc(Pageable pageable);

    Page< StoryAdmin > findByNameContainingOrderByIdDesc(String search, Pageable pageable);

    Page< StoryAdmin > findByDealStatusOrderByIdDesc(Integer status, Pageable pageable);

    Page< StoryAdmin > findByDealStatusAndNameContainingOrderByIdDesc(Integer status, String search, Pageable pageable);

    Page< StoryAdmin > findByNameContainingAndStatusOrderByIdDesc(String search, Integer status, Pageable pageable);

    Page< StoryAdmin > findByStatusOrderByIdDesc(Integer status, Pageable pageable);
}
