package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Đời Không Như Là Mơ
 */
public interface TopConverter {
    
    Long getId();
    
    @Value("#{@myComponent.getDisplayName(target.username,target.displayName)}")
    String getDisplayName();
    
    String getAvatar();
    
    Long getCnt();
    
    Long getScnt();

}
