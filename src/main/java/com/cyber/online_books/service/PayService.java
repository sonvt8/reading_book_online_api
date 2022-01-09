package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;

import java.util.Date;

/**
 * @author Cyber_Group
 */
public interface PayService {
    
    /**
     * Lưu truyện
     *
     * @param story
     * @param chapter
     * @param userSend
     * @param userReceived
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    boolean savePay(Story story, Chapter chapter,
                    User userSend, User userReceived, Integer vote,
                    Double money, Integer payType);

    /**
     * Thực Hiện Giao Dịch Nạp Tiền cho User
     *
     * @param userSend     - Người Nạp
     * @param money        - Số đậu nạp
     * @param userReceived - Người nhận
     */
    void savePayChange(User userSend, Double money, User userReceived);

    /**
     * Kiểm tra User đã thanh toán Chapter Vip trong khoảng
     *
     * @param chapterId
     * @param userId
     * @param startDate
     * @param endDate
     * @return true - nếu đã thanh toán trong khoảng /false - nếu chưa thanh toán / hoặc thanh toán ngoài khoảng
     */
    boolean checkDealChapterVip(Long chapterId, Long userId, Date startDate, Date endDate);
}
