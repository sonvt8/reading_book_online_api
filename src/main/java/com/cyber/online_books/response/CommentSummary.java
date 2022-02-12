package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Cyber_Group
 * @project book_online
 */
public interface CommentSummary {
    
    Long getId();
    
    String getContent();
    
    @Value(value = "#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();
    
    UserSummary getUser();
}
