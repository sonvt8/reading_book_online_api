package com.cyber.online_books.service.impl;

import com.cyber.online_books.repository.ChapterRepository;
import com.cyber.online_books.repository.StoryRepository;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository, StoryRepository storyRepository) {
        this.chapterRepository = chapterRepository;
        this.storyRepository = storyRepository;
    }

    /**
     * Tìm Kiếm Chapter theo
     *
     * @param storyId
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Page< ChapterOfStory > findByStoryIdAndUserId(Long storyId, Long userId, Integer type, Integer pagenumber) {
        Pageable pageable;
        if (type == 1)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("serial"));
        else if (type == 2)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("createDate").descending());
        else if (type == 3)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("createDate"));
        else
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("serial").descending());
        return chapterRepository.findByUser_IdAndStory_Id(userId, storyId, pageable);
    }

    /**
     * Lấy danh sách Chapter của Truyện Theo
     *
     * @param storyId
     * @param pagenumber
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    @Override
    public Page< ChapterOfStory > getListChapterOfStory(Long storyId,
                                                        Integer pagenumber,
                                                        List< Integer > listChapterStatus,
                                                        Integer type) {
        Page<ChapterOfStory> chapterOfStoryPage;
        if (type == 1) {
            Pageable pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_CHAPTER_OF_STORY);
            chapterOfStoryPage = chapterRepository
                    .findByStory_IdAndStatusInOrderBySerialDesc(storyId, listChapterStatus, pageable);
        } else {
            List<ChapterOfStory> chapterOfStoryList = chapterRepository
                    .findByStory_IdAndStatusInOrderBySerialDesc(storyId, listChapterStatus);
            chapterOfStoryPage = new PageImpl<>(chapterOfStoryList);
        }
        return chapterOfStoryPage;
    }
}
