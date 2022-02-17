package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface FollowSummary {
    StoryFollow getStory();

    @Value("#{@myComponent.getChapterReading(target.story.id, target.user.id)}")
    Long getChapterId();

    public interface StoryFollow {

        Long getId();

        String getName();

        Date getUpdateDate();

    }
}
