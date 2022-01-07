package com.cyber.online_books.service;

import com.cyber.online_books.response.FollowSummary;
import org.springframework.data.domain.Page;

public interface UserFollowService {

    /**
     * @param id
     * @param pagenumber
     * @return Page< FollowSummar >
     */
    Page<FollowSummary> findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size);
}
