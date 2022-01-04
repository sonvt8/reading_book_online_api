package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.NotFoundException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.repository.CategoryRepository;
import com.cyber.online_books.repository.StoryRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.response.*;
import com.cyber.online_books.service.CloudinaryService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsListUtils;
import com.cyber.online_books.utils.ConstantsPayTypeUtils;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository, CategoryRepository categoryRepository, CloudinaryService cloudinaryService, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }


    @Override
    public Story findStoryById(Long id) {
        return storyRepository.findById(id).orElse(null);
    }

    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param cID
     * @param page
     * @param size
     * @param storyStatus
     * @param chapterStatus
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryNewUpdateByCategoryId(Integer cID,
                                                              int page, int size,
                                                              List< Integer > storyStatus, List< Integer > chapterStatus) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findStoryNewByCategory(cID, storyStatus, chapterStatus, pageable);
    }

    /**
     * Lấy List Truyện đăng bởi User
     *
     * @param id         - id của User đăng
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @param status     - Trạng Thái Truyện
     * @return
     */
    @Override
    public Page< StoryUser > findPageStoryByUser(Long id, int pagenumber, Integer size, Integer status) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findByUser_IdAndStatusOrderByUpdateDateDesc(id, status, pageable);
    }

    /**
     * Lấy List Truyện Top View theo Category
     *
     * @param categoryId
     * @param historyStatus
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    @Override
    public Page< StoryTop > findStoryTopViewByCategoryId(Integer categoryId, Integer historyStatus,
                                                         List< Integer > listStatus,
                                                         Date startDate, Date endDate,
                                                         int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopViewByCategory(categoryId, historyStatus,
                        listStatus, startDate, endDate, pageable);
    }

    /**
     * Lấy Danh sách Truyện Top  Đề Cử Theo Category
     *
     * @param categoryID
     * @param storyStatus
     * @param payType
     * @param payStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    @Override
    public Page< StoryTop > findStoryTopVoteByCategoryId(Integer categoryID,
                                                         List< Integer > storyStatus,
                                                         Integer payType, Integer payStatus,
                                                         Date startDate, Date endDate,
                                                         int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopVoteByCategory(categoryID, storyStatus, payType, payStatus, startDate, endDate, pageable);
    }

    /**
     * Lấy danh sách truyên top view trong khoảng theo status
     *
     * @param listStatus    - Danh sách trạng thái Story
     * @param startDate     - Ngày Bắt đầu
     * @param endDate       - Ngày kết thúc
     * @param historyStatus - Status history
     * @param page          - số trang
     * @param size          - độ dài trang
     * @return
     */
    @Override
    public Page< StoryTop > findStoryTopViewByStatuss(List< Integer > listStatus,
                                                      Date startDate, Date endDate,
                                                      Integer historyStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository.findStoryTopViewByStatus(listStatus, startDate, endDate, historyStatus, pageable);
    }

    /**
     * Lấy Danh sách Truyện Vip mới cập nhật
     *
     * @param listChapterStatus - danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái truyện
     * @param sDealStatus       - trạng thái truyện trả tiền
     * @param pagenumber        - biến số trang
     * @param size              - biến size
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryVipUpdateByStatus(List< Integer > listChapterStatus, List< Integer > listStoryStatus, Integer sDealStatus, int pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findVipStoryNew(listChapterStatus, listStoryStatus, sDealStatus, pageable);
    }

    /**
     * Lấy Page Truyện theo Status
     *
     * @param listChapterStatus -  danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái Story
     * @param page              - số trang
     * @param size              - độ dài trang
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryUpdateByStatus(List< Integer > listChapterStatus,
                                                       List< Integer > listStoryStatus,
                                                       int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getPageStoryComplete(listChapterStatus, listStoryStatus, pageable);
    }

    /**
     * Lấy List Truyện Top Đề cử Trong Khoảng
     *
     * @param page
     * @param size
     * @param startDate
     * @param endDate
     * @return Page<TopStory>
     */
    @Override
    public Page< StoryTop > getTopStoryAppoind(int page, int size, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopStoryAppoind(ConstantsListUtils.LIST_STORY_DISPLAY, startDate, endDate, ConstantsPayTypeUtils.PAY_APPOINT_TYPE, ConstantsStatusUtils.PAY_COMPLETED, pageable);
    }

    /**
     * Tìm Truyện Theo StoryID và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return StorySummar - nếu tồn tại truyện thỏa mãn điều kiện
     */
    @Override
    public StorySummary findStoryByStoryIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception {
        return storyRepository
                .findByIdAndStatusIn(storyId, listStoryStatus)
                .orElseThrow(NotFoundException::new);
    }


    @Override
    public Page< StoryAdmin > findStoryInAdmin(Integer pagenumber, Integer size, Integer type, String search) {
        Pageable pageable = PageRequest.of(pagenumber-1, size);
        if (type == -1) {
            if (search.trim().isEmpty()) {
                return storyRepository.findByOrderByIdDesc(pageable);
            }
            return storyRepository.findByNameContainingOrderByIdDesc(search, pageable);
        } else if (type == 3) {
            if (search.trim().isEmpty()) {
                return storyRepository.findByDealStatusOrderByIdDesc(ConstantsStatusUtils.STORY_STATUS_GOING_ON, pageable);
            }
            return storyRepository.findByDealStatusAndNameContainingOrderByIdDesc(ConstantsStatusUtils.STORY_STATUS_GOING_ON, search, pageable);
        } else {
            if (search.trim().isEmpty()) {
                return storyRepository.findByStatusOrderByIdDesc(type, pageable);
            }
            return storyRepository.findByNameContainingAndStatusOrderByIdDesc(search, type, pageable);
        }
    }

    @Override
    public boolean deleteStory(Long id) {
        try {
            storyRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Story addNewStory(String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws UserNotLoginException, NotAnImageFileException, HttpMyException {
        checkUnique(null, name);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User userPosted = userRepository.findUserByUsername(currentUsername);

        if (userPosted.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        Story story = new Story();
        story.setName(name);
        story.setAuthor(author);
        story.setInfomation(infomation);
        story.setUser(userPosted);
        story.setCategoryList(Arrays.stream(category).map(r -> categoryRepository.findCategoryByNameAndStatus(r, ConstantsStatusUtils.CATEGORY_ACTIVED)).collect(Collectors.toSet()));
        saveImage(story, image, principal);

        return storyRepository.save(story);
    }

    @Override
    public Story updateAccountStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story storyEdit = storyRepository.findById(id).orElse(null);
        if(storyEdit == null){
            throw new HttpMyException("không tìm thấy truyện");
        }
        checkUnique(id, name);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User userPosted = userRepository.findUserByUsername(currentUsername);

        if (userPosted.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        storyEdit.setName(name);
        storyEdit.setAuthor(author);
        storyEdit.setInfomation(infomation);
        storyEdit.setUser(userPosted);
        storyEdit.setUpdateDate(DateUtils.getCurrentDate());
        storyEdit.setCategoryList(Arrays.stream(category).map(r -> categoryRepository.findCategoryByNameAndStatus(r, ConstantsStatusUtils.CATEGORY_ACTIVED)).collect(Collectors.toSet()));
        saveImage(storyEdit, image, principal);

        return storyRepository.save(storyEdit);
    }

    @Override
    public Story updateAdminStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Double price, Integer timeDeal, Integer dealStatus, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story storyEdit = storyRepository.findById(id).orElse(null);
        if(storyEdit == null){
            throw new HttpMyException("không tìm thấy truyện");
        }
        checkUnique(id, name);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userRepository.findUserByUsername(currentUsername);

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }

        storyEdit.setName(name);
        storyEdit.setAuthor(author);
        storyEdit.setInfomation(infomation);
        storyEdit.setUser(storyEdit.getUser());
        storyEdit.setUpdateDate(DateUtils.getCurrentDate());
        storyEdit.setCategoryList(Arrays.stream(category).map(r -> categoryRepository.findCategoryByNameAndStatus(r, ConstantsStatusUtils.CATEGORY_ACTIVED)).collect(Collectors.toSet()));
        saveImage(storyEdit, image, principal);

        if(dealStatus !=null){
            storyEdit.setDealStatus(dealStatus);
            if(dealStatus == ConstantsStatusUtils.STORY_VIP){
                if(price != null){
                    storyEdit.setPrice(price);
                }
                if(timeDeal != null){
                    storyEdit.setTimeDeal(timeDeal);
                }
            }
        }

        return storyRepository.save(storyEdit);
    }

    private void saveImage(Story story, MultipartFile image, Principal principal) throws NotAnImageFileException {
        if(image != null){
            if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE).contains(image.getContentType())) {
                throw new NotAnImageFileException(image.getOriginalFilename() + " không phải là file hình");
            }
            if(story.getImages() != null)
                story.setImages(story.getImages());
            else{
                String url = cloudinaryService.upload(image, principal.getName() + "-" + System.nanoTime());
                story.setImages(url);
            }

        }
    }

    private Story checkUnique(Long id, String newStoryName) throws HttpMyException {

        boolean isCreatingNew = (id == null || id == 0);
        Story newStoryByName = storyRepository.findStoryByName(newStoryName);

        if(newStoryByName != null){
            if (isCreatingNew) {
                throw new HttpMyException("truyện này đã tồn tại");
            } else {
                if (newStoryByName.getId() != id) {
                    throw new HttpMyException("truyện này đã tồn tại");
                }
            }
            return newStoryByName;
        }
        return null;
    }
}
