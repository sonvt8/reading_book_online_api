package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

public interface InfoSummary {
    
    Long getId();
    
    String getUsername();
    
    String getDisplayName();
    
    String getNotification();
    
    String getGold();
    
    @Value("#{@myComponent.checkAvatar(target.avatar)}")
    String getAvatar();
    
    @Value("#{@myComponent.countStoryOfUser(target.id)}")
    Long getStory();
    
    @Value("#{@myComponent.maskEmail(target.email)}")
    String getEmail();
    
    @Value("#{@myComponent.countChapterOfUser(target.id)}")
    Long getChapter();
}
