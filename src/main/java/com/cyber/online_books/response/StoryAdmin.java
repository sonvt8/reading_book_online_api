package com.cyber.online_books.response;

import com.cyber.online_books.entity.Category;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

public interface StoryAdmin {
    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    Float getRating();

    Integer getCountView();

    Integer getDealStatus();

    Date getCreateDate();

    List<Category> getCategoryList();

    @Value("#{@myComponent.getDisplayName(target.user.username, target.user.displayName)}")
    String getDisplayName();

    Integer getStatus();
}
