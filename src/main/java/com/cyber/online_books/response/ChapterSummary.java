package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

public interface ChapterSummary {

    Long getId();

    String getChapterNumber();

    Float getSerial();

    String getName();

    Integer getStatus();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();
}
