package com.cyber.online_books.component;

import com.cyber.online_books.response.ChapterSummary;
import com.cyber.online_books.service.ChapterService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.DateUtils;
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

}
