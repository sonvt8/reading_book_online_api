package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Chapter;
import com.cyber.online_books.entity.Pay;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.repository.PayRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.response.PaySummary;
import com.cyber.online_books.service.PayService;
import com.cyber.online_books.utils.ConstantsPayTypeUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Cyber_Group
 */
@Service
public class PayServiceImpl implements PayService {
    Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private PayRepository payRepository;
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Lưu giao dịch
     *
     * @param story
     * @param chapter
     * @param userSend
     * @param userReceived
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    @Override
    @Transactional(noRollbackFor = HttpMyException.class)
    public boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Integer vote, Double money
            , Integer payType) {
        Long chapterID = null;
        Long storyID = null;
        Long userReceivedId = null;
        if (chapter != null)
            chapterID = chapter.getId();
        if (story != null)
            storyID = story.getId();
        if (userReceived != null)
            userReceivedId = userReceived.getId();
        return payRepository
                .transferPayChapter(userSend.getId(),
                        userReceivedId,
                        chapterID,
                        storyID,
                        money, vote,
                        payType);
    }

    /**
     * Thực Hiện Giao Dịch Nạp Tiền cho User
     *
     * @param userSend     - Người Nạp
     * @param money        - Số đậu nạp
     * @param userReceived - Người nhận
     */
    @Override
    @Transactional
    public void savePayChange(User userSend, Double money, User userReceived) {
        Pay pay = new Pay();
        pay.setUserSend(userSend);
        pay.setUserReceived(userReceived);
        pay.setMoney(money);
        pay.setType(ConstantsPayTypeUtils.PAY_RECHARGE_TYPE);
        savePay(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        userReceived = userRepository.findById(userReceived.getId()).get();
        userReceived.setGold(userReceived.getGold() + money);
        userRepository.save(userReceived);
    }

    /**
     * Kiểm tra User đã thanh toán Chapter Vip trong khoảng
     *
     * @param chapterId
     * @param userId
     * @param startDate
     * @param endDate
     * @return true - nếu đã thanh toán trong khoảng /false - nếu chưa thanh toán / hoặc thanh toán ngoài khoảng
     */
    @Override
    public boolean checkDealChapterVip(Long chapterId, Long userId, Date startDate, Date endDate) {
        return payRepository
                .existsByChapter_IdAndUserSend_IdAndCreateDateBetweenAndTypeAndStatus(chapterId, userId,
                        startDate, endDate, ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE, ConstantsStatusUtils.PAY_COMPLETED);
    }

    /**
     * Lưu đề cử truyện
     *
     * @param story
     * @param userSend
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    @Override
    @Transactional(noRollbackFor = HttpMyException.class)
    public boolean savePayAppoint(Story story, User userSend, Double money, Integer vote, Integer payType) {
        Long storyID = null;
        if (story != null)
            storyID = story.getId();
        return payRepository
                .appointPayStory(userSend.getId(),
                        storyID,
                        money, vote,
                        payType);
    }

    private void savePay(Pay pay) {
        payRepository.save(pay);
    }

    /**
     * Lấy danh sách giao dịch của User theo
     *
     * @param id         - id của User
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @return
     */
    @Override
    public Page<PaySummary> findPageByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return payRepository.findByUserReceived_IdOrUserSend_IdOrderByCreateDateDesc(id, id, pageable);
    }

    /**
     * Lấy danh sách giao dịch rút tiền của người dùng
     *
     * @param id
     * @param pagenumber
     * @param size
     * @return
     */
    @Override
    public Page< PaySummary > findPagePayWithdrawByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return payRepository.findByTypeAndUserSend_IdOrderByCreateDateDesc(ConstantsPayTypeUtils.PAY_WITHDRAW_TYPE, id, pageable);
    }

    /**
     * Thực hiện giao dịch đăng ký rút tiền
     *
     * @param user
     * @param money
     */
    @Override
    @Transactional
    public Long savePayDraw(User user, Double money) {
        Pay pay = new Pay();
        pay.setUserSend(user);
        pay.setMoney(money);
        pay.setType(ConstantsPayTypeUtils.PAY_WITHDRAW_TYPE);
        payRepository.save(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        user = userRepository.findById(user.getId()).get();
        user.setGold(user.getGold() - money);
        userRepository.save(user);
        return pay.getId();
    }

    /**
     * Tìm kiếm Pay Theo id
     *
     * @param payId - id Pay
     * @return
     */
    @Override
    public Pay findPayById(Long payId) {
        return payRepository.findById(payId).orElse(null);
    }

    /**
     * Thực Hiện giao dịch đọc chapter Vip
     *
     * @param userSend
     * @param chapter
     */
    @Override
    @Transactional
    public void saveReadingVipPay(User userSend, Chapter chapter) {
        Pay pay = new Pay();
        pay.setUserSend(userSend);
        pay.setChapter(chapter);
        pay.setUserReceived(chapter.getUser());
        pay.setMoney(chapter.getPrice());
        pay.setType(ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE);
        savePay(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        userSend = userRepository.findById(userSend.getId()).get();
        userSend.setGold(userSend.getGold() - chapter.getPrice());
        saveUser(userSend);
        //Lấy Thông tin mới nhất của người nhận
        User userReceived = userRepository.findById(chapter.getUser().getId()).get();
        userSend.setGold(userSend.getGold() - chapter.getPrice());
        userReceived.setGold(userReceived.getGold() + chapter.getPrice());
        saveUser(userReceived);
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }
}