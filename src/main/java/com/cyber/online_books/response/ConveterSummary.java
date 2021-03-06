package com.cyber.online_books.response;

import com.cyber.online_books.entity.Role;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface ConveterSummary {

    Long getId();

    @Value("#{@myComponent.getDisplayName(target.username,target.displayName)}")
    String getDisplayName();

    String getNotification();

    String getAvatar();

    Double getGold();

    Role[] getRoleList();

    Date getCreateDate();

    @Value("#{@myComponent.countChapterOfUser(target.id)}")
    Long getCountChapter();

    @Value("#{@myComponent.countStoryOfUser(target.id)}")
    Long getCountStory();
}
