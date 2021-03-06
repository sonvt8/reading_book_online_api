package com.cyber.online_books.service;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Pay;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.response.PaySummary;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

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

    /**
     * Lưu đề cử truyện
     *
     * @param story
     * @param userSend
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    boolean savePayAppoint(Story story,
                           User userSend, Double money, Integer vote,
                           Integer payType);

    /**
     * Lấy danh sách giao dịch của User theo
     *
     * @param id         - id của User
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @return
     */
    Page<PaySummary> findPageByUserId(Long id, Integer pagenumber, Integer size);

    /**
     * Lấy danh sách giao dịch rút tiền của người dùng
     *
     * @param id
     * @param pagenumber
     * @param size
     * @return
     */
    Page< PaySummary > findPagePayWithdrawByUserId(Long id, Integer pagenumber, Integer size);

    /**
     * Thực hiện giao dịch đăng ký rút tiền
     *
     * @param user
     * @param money
     */
    Long savePayDraw(User user, Double money);

    /**
     * Tìm kiếm Pay Theo id
     *
     * @param payId - id Pay
     * @return
     */
    Pay findPayById(Long payId);

    /**
     * Thực Hiện giao dịch đọc chapter Vip
     *
     * @param userSend
     * @param chapter
     */
    void saveReadingVipPay(User userSend, Chapter chapter);


}
