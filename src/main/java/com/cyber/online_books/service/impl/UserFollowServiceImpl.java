package com.cyber.online_books.service.impl;

import com.cyber.online_books.repository.UserFollowRepository;
import com.cyber.online_books.response.FollowSummary;
import com.cyber.online_books.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserFollowServiceImpl implements UserFollowService {

    @Autowired
    private UserFollowRepository userFollowRepository;

    /**
     * @param id
     * @param pagenumber
     * @return
     */
    @Override
    public Page< FollowSummary > findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return userFollowRepository.findByUser_IdOrderByStory_UpdateDateDesc(id, pageable);
    }
}
