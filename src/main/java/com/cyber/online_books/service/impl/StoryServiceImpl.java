package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Role;
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
import com.cyber.online_books.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.cyber.online_books.utils.ConstantsUtils.ROLE_USER;

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
     * L???y List Truy???n M???i C???p Nh???t theo Category
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
     * L???y List Truy???n ????ng b???i User
     *
     * @param id         - id c???a User ????ng
     * @param pagenumber - bi???n s??? trang
     * @param size       - bi???n size
     * @param status     - Tr???ng Th??i Truy???n
     * @return
     */
    @Override
    public Page< StoryUser > findPageStoryByUser(Long id, int pagenumber, Integer size, Integer status) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findByUser_IdAndStatusOrderByUpdateDateDesc(id, status, pageable);
    }

    /**
     * L???y List Truy???n Top View theo Category
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
     * L???y Danh s??ch Truy???n Top  ????? C??? Theo Category
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
     * L???y danh s??ch truy??n top view trong kho???ng theo status
     *
     * @param listStatus    - Danh s??ch tr???ng th??i Story
     * @param startDate     - Ng??y B???t ?????u
     * @param endDate       - Ng??y k???t th??c
     * @param historyStatus - Status history
     * @param page          - s??? trang
     * @param size          - ????? d??i trang
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
     * L???y Danh s??ch Truy???n Vip m???i c???p nh???t
     *
     * @param listChapterStatus - danh s??ch tr???ng th??i chapter
     * @param listStoryStatus   - danh s??ch tr???ng th??i truy???n
     * @param sDealStatus       - tr???ng th??i truy???n tr??? ti???n
     * @param pagenumber        - bi???n s??? trang
     * @param size              - bi???n size
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryVipUpdateByStatus(List< Integer > listChapterStatus, List< Integer > listStoryStatus, Integer sDealStatus, int pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findVipStoryNew(listChapterStatus, listStoryStatus, sDealStatus, pageable);
    }

    /**
     * L???y Page Truy???n theo Status
     *
     * @param listChapterStatus -  danh s??ch tr???ng th??i chapter
     * @param listStoryStatus   - danh s??ch tr???ng th??i Story
     * @param page              - s??? trang
     * @param size              - ????? d??i trang
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
     * L???y List Truy???n Top ????? c??? Trong Kho???ng
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
     * T??m Truy???n Theo StoryID v?? ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return StorySummar - n???u t???n t???i truy???n th???a m??n ??i???u ki???n
     */
    @Override
    public StorySummary findStoryByStoryIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception {
        return storyRepository
                .findByIdAndStatusIn(storyId, listStoryStatus)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * L???y Danh s??ch truy???n m???i ????ng c???a Converter
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    @Override
    public List< StorySlide > findStoryOfConverter(Long userId, List< Integer > listStoryDisplay) {
        return storyRepository
                .findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStoryDisplay);
    }

    /**
     * T??m Truy???n Theo Id v?? ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return Story - n???u t???n t???i truy???n th???a m??n ??i???u ki???n
     */
    @Override
    public Story findStoryByIdAndStatus(Long storyId, List< Integer > listStoryStatus) {
        return storyRepository
                .findStoryByIdAndStatusIn(storyId, listStoryStatus)
                .orElse(null);
    }

    /**
     * L???y List Truy???n Theo searchText
     *
     * @param searchText
     * @param listStatus
     * @return
     */
    @Override
    public List< StorySlide > findListStoryBySearchKey(String searchText, List< Integer > listStatus) {
        return storyRepository
                .findTop10ByNameContainingAndStatusInOrderByNameAsc(searchText, listStatus);
    }

    @Override
    public Page< StoryMember > findStoryByUserId(Long userId, List< Integer > listStatus,
                                                 int pagenumber, int type, Integer size) {
        Page< StoryMember > storyMembers;
        if (type == 1) {
            Pageable pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_CHAPTER_OF_STORY);
            storyMembers = storyRepository.findByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStatus, pageable);
        } else {
            List< StoryMember > storyMemberList = storyRepository
                    .findAllByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStatus);
            storyMembers = new PageImpl<>(storyMemberList);
        }
        return storyMembers;
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
//        for (Role role : userPosted.getRoleList()) {
//            if (role.getId() == ROLE_USER)
//                throw new AccessDeniedException("B???n kh??ng ????? quy???n ????? th???c hi???n h??nh ?????ng n??y");
//        }

        if (userPosted.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
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
    public Story updateAccountStory(Long id, String name, String author, String infomation, String[] category, Integer status, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story storyEdit = storyRepository.findById(id).orElse(null);

        if(storyEdit == null){
            throw new HttpMyException("kh??ng t??m th???y truy???n");
        }

        checkUnique(id, name);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User userPosted = userRepository.findUserByUsername(currentUsername);

        if (!storyEdit.getUser().getId().equals(userPosted.getId())){
            throw new HttpMyException("B???n kh??ng c?? quy???n s???a truy???n kh??ng do b???n ????ng!");
        }

        if (userPosted.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        storyEdit.setName(name);
        storyEdit.setAuthor(author);
        storyEdit.setInfomation(infomation);
        storyEdit.setUser(userPosted);
        storyEdit.setStatus(status);
        storyEdit.setUpdateDate(DateUtils.getCurrentDate());
        storyEdit.setCategoryList(Arrays.stream(category).map(r -> categoryRepository.findCategoryByNameAndStatus(r, ConstantsStatusUtils.CATEGORY_ACTIVED)).collect(Collectors.toSet()));
        saveImage(storyEdit, image, principal);

        return storyRepository.save(storyEdit);
    }

    @Override
    public Story updateAdminStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Double price, Integer timeDeal, Integer dealStatus, Integer status, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException {
        Story storyEdit = storyRepository.findById(id).orElse(null);
        if(storyEdit == null){
            throw new HttpMyException("kh??ng t??m th???y truy???n");
        }
        checkUnique(id, name);
        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User user = userRepository.findUserByUsername(currentUsername);

        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpMyException("T??i kho???n c???a b???n ???? b??? kh??a m???i li??n h??? admin ????? bi???t th??m th??ng tin");
        }

        storyEdit.setName(name);
        storyEdit.setAuthor(author);
        storyEdit.setInfomation(infomation);
        //storyEdit.setUser(storyEdit.getUser());
        storyEdit.setStatus(status);
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

    /**
     * L???y s??? l?????ng truy???n ????ng b???i User
     *
     * @param userId
     * @param listStoryDisplay
     * @return Long
     */
    @Override
    public Long countStoryByUser(Long userId, List< Integer > listStoryDisplay) {
        return storyRepository.countByUser_IdAndStatusIn(userId, listStoryDisplay);
    }

    /**
     * @param date
     * @return
     */
    @Override
    public Long countNewStoryInDate(Date date) {
        return storyRepository.countByCreateDateGreaterThanEqual(date);
    }

    private void saveImage(Story story, MultipartFile image, Principal principal) throws NotAnImageFileException {
        if(image != null){
            if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE).contains(image.getContentType())) {
                throw new NotAnImageFileException(image.getOriginalFilename() + " kh??ng ph???i l?? file h??nh");
            }
            String url = cloudinaryService.uploadCover(image, story.getName());
            story.setImages(url);
        }
    }

    private Story checkUnique(Long id, String newStoryName) throws HttpMyException {

        boolean isCreatingNew = (id == null || id == 0);
        Story newStoryByName = storyRepository.findStoryByName(newStoryName);

        if(newStoryByName != null){
            if (isCreatingNew) {
                throw new HttpMyException("truy???n n??y ???? t???n t???i");
            } else {
                if (newStoryByName.getId() != id) {
                    throw new HttpMyException("truy???n n??y ???? t???n t???i");
                }
            }
            return newStoryByName;
        }
        return null;
    }
}
