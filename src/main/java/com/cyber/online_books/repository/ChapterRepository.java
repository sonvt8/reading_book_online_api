package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.response.ChapterOfStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChapterRepository extends JpaRepository<Chapter, Long > {

    /**
     *
     * @param userId
     * @param storyId
     * @param pageable
     * @return
     */
    Page<ChapterOfStory> findByUser_IdAndStory_Id(Long userId, Long storyId, Pageable pageable);
}
