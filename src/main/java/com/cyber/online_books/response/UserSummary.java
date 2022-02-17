package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Cyber_Group
 * @project book_online
 */
public interface UserSummary {
    
    Long getId();
    
    @Value("#{@myComponent.getDisplayName(target.username, target.displayName)}")
    String getDisplayName();
    
    String getAvatar();
}
