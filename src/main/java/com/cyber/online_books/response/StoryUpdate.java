package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

public interface StoryUpdate {

    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    @Value("#{@myComponent.getBetewwen(target.update_date)}")
    String getTimeUpdate();

    Long getChapterId();

    String getInfomation();

    Integer getChapterNumber();

    @Value("#{@myComponent.getDisplayName(target.username, target.display_name)}")
    String getDisplayName();

    Integer getDealStatus();
}

