package com.cyber.online_books.component;

import com.cyber.online_books.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {
    private final static Logger logger = LoggerFactory.getLogger(MyComponent.class);

    @Autowired
    private StoryService storyService;

    public String getDisplayName(String username, String displayName) {
        return (displayName != null && !displayName.isEmpty()) ? displayName : username;
    }
}
