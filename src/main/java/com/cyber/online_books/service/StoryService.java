package com.cyber.online_books.service;

import com.cyber.online_books.entity.Story;
import com.cyber.online_books.exception.HttpMyException;
import com.cyber.online_books.exception.domain.NotAnImageFileException;
import com.cyber.online_books.exception.domain.UserNotLoginException;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface StoryService {
    Story findStoryById(Long id);

    Story addNewStory(String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws UserNotLoginException, NotAnImageFileException, HttpMyException;

    Story updateAccountStory(Long id, String name, String author, String infomation, String[] category, MultipartFile image, Principal principal) throws HttpMyException, UserNotLoginException, NotAnImageFileException;
}
