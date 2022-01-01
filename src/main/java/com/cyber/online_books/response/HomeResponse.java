package com.cyber.online_books.response;

import lombok.Data;

import java.util.List;

@Data
public class HomeResponse {
    private List<StoryTop> topStoryWeek;
    private List<StoryUpdate> topVipStory;
}
