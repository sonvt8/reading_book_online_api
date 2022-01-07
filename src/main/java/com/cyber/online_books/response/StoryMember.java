package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

public interface StoryMember {
    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    Long getCountView();

    Integer getDealStatus();

    @Value("#{@myComponent.getBetewwen(target.updateDate)}")
    String getTimeUpdate();

    @Value("#{@myComponent.getNewChapter(target.id)}")
    ChapterSummary getNewChapter();
}
