package com.cyber.online_books.response;

import com.cyber.online_books.entity.Chapter;
import lombok.Data;

@Data
public class StoryDetailResponse {
    private StorySummary storySummary;
    private Long countRating;
}
