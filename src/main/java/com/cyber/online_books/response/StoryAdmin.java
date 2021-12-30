package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface StoryAdmin {
    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    Float getRating();

    Integer getCountView();

    Integer getDealStatus();

    Date getCreateDate();

    @Value("#{@myComponent.getDisplayName(target.user.username, target.user.displayName)}")
    String getDisplayName();

    Integer getStatus();
}
