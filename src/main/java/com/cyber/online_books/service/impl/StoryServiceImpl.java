package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import com.cyber.online_books.repository.CategoryRepository;
import com.cyber.online_books.repository.StoryRepository;
import com.cyber.online_books.repository.UserRepository;
import com.cyber.online_books.service.CloudinaryUploadService;
import com.cyber.online_books.service.StoryService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Story addNewStory(String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException {
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
        String url = cloudinaryUploadService.upload(image, currentUsername + "-" + System.nanoTime());
        story.setImages(url);
        return storyRepository.save(story);
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
