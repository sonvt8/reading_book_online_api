package com.cyber.online_books.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class HomeResponse {
    private List<StoryTop> topStoryWeek;
    private List<StoryUpdate> topVipStory;
    private List< StoryUpdate > listNewStory;
    private List< StoryTop > topStory;

}
