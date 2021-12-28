package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.repository.CategoryRepository;
import com.cyber.online_books.repository.StoryRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.response.StoryUser;
import com.cyber.online_books.service.CloudinaryUploadService;
import com.cyber.online_books.service.StoryService;
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
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryUploadService cloudinaryUploadService;
    private final UserRepository userRepository;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository, CategoryRepository categoryRepository, CloudinaryUploadService cloudinaryUploadService, UserRepository userRepository) {
        this.storyRepository = storyRepository;
        this.categoryRepository = categoryRepository;
        this.cloudinaryUploadService = cloudinaryUploadService;
        this.userRepository = userRepository;
    }


    @Override
    public Story findStoryById(Long id) {
        return storyRepository.findById(id).orElse(null);
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
        Story storyEdit = checkUnique(id, name);
        if(storyEdit == null){
            throw new HttpMyException("Not found story for update");
        }

        if (principal == null) {
            throw new UserNotLoginException();
        }

        String currentUsername = principal.getName();
        User userPosted = userRepository.findUserByUsername(currentUsername);

        storyEdit.setName(name);
        storyEdit.setAuthor(author);
        storyEdit.setInfomation(infomation);
        storyEdit.setUser(userPosted);
        storyEdit.setUpdateDate(DateUtils.getCurrentDate());
        storyEdit.setCategoryList(Arrays.stream(category).map(r -> categoryRepository.findCategoryByNameAndStatus(r, ConstantsStatusUtils.CATEGORY_ACTIVED)).collect(Collectors.toSet()));
        saveImage(storyEdit, image, principal);

        return storyRepository.save(storyEdit);
    }

    private void saveImage(Story story, MultipartFile image, Principal principal) throws NotAnImageFileException {
        if(image != null){
            if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE).contains(image.getContentType())) {
                throw new NotAnImageFileException(image.getOriginalFilename() + " is not an image file");
            }
            if(story.getImages() != null)
                story.setImages(story.getImages());
            else{
                String url = cloudinaryUploadService.upload(image, principal.getName() + "-" + System.nanoTime());
                story.setImages(url);
            }

        }
    }

    private Story checkUnique(Long id, String newStoryName) throws HttpMyException {

        boolean isCreatingNew = (id == null || id == 0);
        Story newStoryByName = storyRepository.findStoryByName(newStoryName);

        if (isCreatingNew) {
            if (newStoryByName != null) {
                throw new HttpMyException("Story already exist");
            }
        } else {
            if (newStoryByName != null && newStoryByName.getId() != id) {
                throw new HttpMyException("Story already exist");
            }
        }

        return newStoryByName;
    }
}
