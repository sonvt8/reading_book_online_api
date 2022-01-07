package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface StorySummary {

    Long getId();

    String getName();

    String getImages();

    String getAuthor();

    //Lấy Thông Tin
    String getInfomation();

    //Lấy điểm đề cử
    Long getCountAppoint();

    //Lấy Điểm đánh giá
    Float getRating();

    //Lấy ID Converter
    @Value("#{target.user.id}")
    Long getUserId();

    //Lấy Chapter Đầu Tiên
    @Value("#{@myComponent.getChapterHead(target.id)}")
    ChapterSummary getChapterHead();

    //Lấy Chapter Mới Nhất
    @Value("#{@myComponent.getNewChapter(target.id)}")
    ChapterSummary getChapterNew();

    List< CategorySummary > getCategoryList();

    Integer getStatus();
}
