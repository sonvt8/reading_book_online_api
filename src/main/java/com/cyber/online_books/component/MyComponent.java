package com.cyber.online_books.component;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Role;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.response.ChapterSummary;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.HistoryService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import com.cyber.online_books.utils.DateUtils;
import com.cyber.online_books.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyComponent {
    private final static Logger logger = LoggerFactory.getLogger(MyComponent.class);

    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private HistoryService historyService;

    public String getDisplayName(String username, String displayName) {
        return (displayName != null && !displayName.isEmpty()) ? displayName : username;
    }

    public String getBetewwen(Date date) {
        if(date == null)
            return "";
        return DateUtils.betweenTwoDays(date);
    }

    //Lấy Chapter Đầu Tiên của Truyện
    public ChapterSummary getChapterHead(Long storyId) {
        return chapterService
                .findChapterHeadOfStory(storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    //Lấy Chapter Mới Nhất của Truyện
    public ChapterSummary getNewChapter(Long storyId) {
        return chapterService
                .findChapterNewOfStory(storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }

    public Long getChapterReading(Long storyId, Long userId) {
        Chapter chapter = historyService.findChapterReadByUser(userId, storyId);
        if (chapter != null)
            return chapter.getId();
        return null;
    }

    public boolean hasRole(User use, Integer id) {
        for (Role role : use.getRoleList()) {
            if (role.getId() == id)
                return true;
        }
        return false;
    }

    public String checkAvatar(final String avatar) {
        if (avatar == null || avatar.isEmpty()) {
            return ConstantsUtils.AVATAR_DEFAULT;
        }
        return avatar;
    }

    public String maskEmail(final String email) {
        try {
            String[] parts = email.split("@");

            if (parts[0].length() < 2)
                return email;
            else
                return WebUtils.maskString(parts[0], '*') + "@" + parts[1];
        } catch (Exception ex) {
            return email;
        }
    }

    public Long countStoryOfUser(Long uID) {
        return storyService.
                countStoryByUser(uID, ConstantsListUtils.LIST_STORY_DISPLAY);
    }

    public Long countChapterOfUser(Long uID) {
        return chapterService.
                countChapterByUser(uID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
    }
}
