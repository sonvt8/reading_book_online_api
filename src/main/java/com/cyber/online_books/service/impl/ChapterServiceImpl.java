package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.repository.ChapterRepository;
import com.cyber.online_books.repository.StoryRepository;
import com.cyber.online_books.response.ChapterOfStory;
import com.cyber.online_books.response.ChapterSummary;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

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

    @Override
    public Chapter saveNewChapter(Chapter chapter, Long id) {
        chapter.setWordCount(WebUtils.countWords(chapter.getContent()));
        String content = chapter.getContent().replaceAll("\n", "\r\n");
        chapter.setContent(content.replaceAll("\n", "<br />"));
        chapter.setName(chapter.getName());
        Chapter newChapter = chapterRepository.save(chapter);
        if (newChapter.getId() != null) {
            Story story = storyRepository.findById(id).get();
            story.setUpdateDate(newChapter.getCreateDate());
            storyRepository.save(story);
        }
        return newChapter;
    }

    @Override
    public Chapter updateChapter(Chapter chapter, Long id) {
        try {
            Chapter editChapter = chapterRepository.findById(id).get();
            editChapter.setWordCount(WebUtils.countWords(chapter.getContent()));
            editChapter.setSerial(chapter.getSerial());
            editChapter.setStatus(chapter.getStatus());
            editChapter.setName(chapter.getName());
            editChapter.setChapterNumber(chapter.getChapterNumber());
            editChapter.setContent(chapter.getContent());
            chapterRepository.save(editChapter);
            return editChapter;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Chapter findChapterById(Long id) {
        return chapterRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteChapter(Long id) {
        try {
            chapterRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
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

    /**
     * Lấy Chapter ID Chương Đầu
     *
     * @param storyId
     * @param listStatus
     * @return Long
     */
    @Override
    public ChapterSummary findChapterHeadOfStory(Long storyId, List< Integer > listStatus) {
        return chapterRepository.findChapterHead(storyId, listStatus).orElse(null);
    }

    /**
     * Tìm Chapter Mới Nhất Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    @Override
    public ChapterSummary findChapterNewOfStory(Long storyId, List< Integer > listStatus) {
        return chapterRepository.findChapterNew(storyId, listStatus).orElse(null);
    }

    /**
     * Tìm Chapter Theo Story ID và Chapter ID
     *
     * @param storyId
     * @param listStatusStory
     * @param chapterId
     * @param listStatusChapter
     * @return Chapter
     * @throws Exception
     */
    @Override
    public Chapter findChapterByStoryIdAndChapterID(Long storyId, List< Integer > listStatusStory, Long chapterId, List< Integer > listStatusChapter) throws Exception {
        return chapterRepository
                .findByStory_IdAndStory_StatusInAndIdAndStatusIn(storyId, listStatusStory,
                        chapterId, listStatusChapter)
                .orElseThrow(() -> new HttpMyException("Chương không tồn tại hoặc đã bị xóa!"));
    }

    /**
     * Cập Nhật Lượt Xem Của Chapter
     *
     * @param chapter
     * @throws Exception
     */
    @Override
    public void updateViewChapter(Chapter chapter) throws Exception {
        Chapter updateChapter = findChapterByStoryIdAndChapterID(chapter.getStory().getId(), ConstantsListUtils.LIST_STORY_DISPLAY,
                chapter.getId(), ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        updateChapter.setCountView(updateChapter.getCountView() + 1);
        Story story = updateChapter.getStory();
        story.setCountView(story.getCountView() + 1);
        updateChapter.setStory(story);
        chapterRepository.save(updateChapter);
    }

    /**
     * Lấy Chapter ID Trước
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    @Override
    public Long findPreviousChapterID(Float serial, Long storyId) {
        Optional< Long > previousID = chapterRepository
                .findPreviousChapter(serial, storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return previousID.orElseGet(() -> valueOf(0));
    }

    /**
     * Lấy Chapter ID Tiếp Theo
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    @Override
    public Long findNextChapterID(Float serial, Long storyId) {
        Optional< Long > nextId = chapterRepository
                .findNextChapter(serial, storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return nextId.orElseGet(() -> valueOf(0));
    }

    /**
     * Tìm kiếm Chapter theo
     *
     * @param chapterId  - ID của chapter
     * @param listStatus -  List các Trạng Thái của Chapter
     * @return chapter - nếu có dữ liệu thỏa mãn điều kiện / null - nếu không có dữ liệu thỏa mãn điều kiện
     */
    @Override
    public Chapter findChapterByIdAndStatus(Long chapterId, List< Integer > listStatus) {
        return chapterRepository.findChapterByIdAndStatusIn(chapterId, listStatus)
                .orElse(null);
    }

    /**
     * Lấy số lượng chương đã đăng thành công của User
     *
     * @param userId
     * @param listChapterDisplay
     * @return long
     */
    @Override
    public Long countChapterByUser(Long userId, List< Integer > listChapterDisplay) {
        return chapterRepository.countByUser_IdAndStatusIn(userId, listChapterDisplay);
    }

    /**
     * @param date
     * @return
     */
    @Override
    public Long countNewChapterInDate(Date date) {
        return chapterRepository.countByCreateDateGreaterThanEqual(date);
    }
}
