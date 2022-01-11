package com.cyber.online_books.response;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface PaySummary {
    //Id giao dịch
    Long getId();

    //Id người gửi
    @Value("#{target.userSend}")
    UserPay getSendId();

    //Id người nhận
    @Value("#{target.userReceived}")
    UserPay getReceivedId();

    //Nội dung thanh toán
    StoryPay getStory();

    ChapterPay getChapter();

    //Số tiền giao dịch
    Double getMoney();

    //Ngày giao dịch
    Date getCreateDate();

    //Loại giao dịch
    Integer getType();

    //Số phiếu đề cử
    Integer getVote();

    //Trạng Thái Giao dịch
    Integer getStatus();

    interface UserPay {
        Long getId();
    }

    interface StoryPay {
        Long getId();

        String getName();
    }

    interface ChapterPay {
        Long getId();

        String getChapterNumber();

        StoryPay getStory();
    }
}
