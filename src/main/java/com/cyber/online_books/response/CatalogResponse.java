package com.cyber.online_books.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CatalogResponse {
    private List<StoryTop> topStoryMonth;
    private Page<StoryUpdate> listStoryPage;
}
