package com.cyber.online_books.response;

import com.cyber.online_books.entity.Chapter;
import lombok.Data;

@Data
public class CheckStoryDetail {
    private Chapter readChapter;
    private boolean checkConverter;
    private boolean rating;
}
