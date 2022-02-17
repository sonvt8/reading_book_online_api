package com.cyber.online_books.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class StoryByCategoryId {
    private Page<StoryUpdate> pageStory;
    private List< StoryTop > listTopViewWeek;
    private List< StoryTop > listTopAppointMonth;
}
