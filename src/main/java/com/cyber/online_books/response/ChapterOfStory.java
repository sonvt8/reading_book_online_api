package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface ChapterOfStory {
    Long getId();

    Integer getChapterNumber();

    Float getSerial();

    String getName();

    @Value("#{target.story.id}")
    Long getStoryId();

    @Value("#{@myComponent.getDisplayName(target.user.username,target.user.displayName)}")
    String getDisplayName();

    Date getCreateDate();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();

    Integer getStatus();

    Double getPrice();
}
