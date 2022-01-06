package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.History;
import com.cyber.online_books.repository.HistoryRepository;
import com.cyber.online_books.service.HistoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * Lấy Chapter Mới đọc
     *
     * @param userId
     * @param storyId
     * @return ChapterSummary
     */
    @Override
    public Chapter findChapterReadByUser(Long userId, Long storyId) {
        Optional<History> ufavorites = historyRepository
                .findTopByUser_IdAndChapter_Story_IdAndChapter_StatusInOrderByDateViewDesc(userId, storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return ufavorites.map(History::getChapter).orElse(null);
    }
}
