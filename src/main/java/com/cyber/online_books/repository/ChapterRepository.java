package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.response.ChapterOfStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long > {

    /**
     *
     * @param userId
     * @param storyId
     * @param pageable
     * @return
     */
    Page<ChapterOfStory> findByUser_IdAndStory_Id(Long userId, Long storyId, Pageable pageable);

    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @param pageable
     * @return Page<ChapterOfStory>
     */
    Page< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus,
                                                                      Pageable pageable);

    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    List< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus);
}
