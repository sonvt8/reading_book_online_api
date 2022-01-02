package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.repository.PayRepository;
import com.cyber.online_books.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cyber_Group
 */
@Service
public class PayServiceImpl implements PayService {
    Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private PayRepository payRepository;
    
    /**
     * Lưu giao dịch
     *
     * @param story
     * @param chapter
     * @param userSend
     * @param userReceived
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    @Override
    @Transactional(noRollbackFor = HttpMyException.class)
    public boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Integer vote, Double money
            , Integer payType) {
        Long chapterID = null;
        Long storyID = null;
        Long userReceivedId = null;
        if (chapter != null)
            chapterID = chapter.getId();
        if (story != null)
            storyID = story.getId();
        if (userReceived != null)
            userReceivedId = userReceived.getId();
        return payRepository
                .transferPayChapter(userSend.getId(),
                        userReceivedId,
                        chapterID,
                        storyID,
                        money, vote,
                        payType);
    }
}