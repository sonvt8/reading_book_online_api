package com.cyber.online_books.response;

import com.cyber.online_books.entity.Chapter;
import lombok.Data;

@Data
public class ChapterResponse {
    private Chapter chapter;
    private Long preChapter;
    private Long nextChapter;
    private boolean checkVip;
    private long timeDealDay;

}
