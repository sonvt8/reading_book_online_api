package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Comment;
import com.cyber.online_books.response.CommentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cyber_Group
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
    
    /**
     * Lấy Page Comment của truyện
     *
     * @param storyId
     * @param status
     * @param pageable
     * @return Page<CommentSummary>
     */
    Page<CommentSummary> findByStory_IdAndStatusOrderByCreateDateDesc(Long storyId, Integer status, Pageable pageable);
    
    /**
     * Lấy List All comment của truyện
     *
     * @param storyId
     * @param status
     * @return List<CommentSummary>
     */
    List< CommentSummary > findByStory_IdAndStatusOrderByCreateDateDesc(Long storyId, Integer status);
}
