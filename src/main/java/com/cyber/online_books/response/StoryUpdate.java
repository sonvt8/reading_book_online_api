package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

public interface StoryUpdate {

    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    @Value("#{@myComponent.getBetewwen(target.updateDate)}")
    String getTimeUpdate();

    Long getChapterId();

    String getInfomation();

    Integer getChapterNumber();

    @Value("#{@myComponent.getDisplayName(target.username, target.displayName)}")
    String getDisplayName();

    Integer getDealStatus();
}

