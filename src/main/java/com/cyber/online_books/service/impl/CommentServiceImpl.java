package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Comment;
import com.cyber.online_books.entity.Story;
import com.cyber.online_books.entity.User;
import com.cyber.online_books.repository.CommentRepository;
import com.cyber.online_books.response.CommentSummary;
import com.cyber.online_books.service.CommentService;
import com.cyber.online_books.utils.ConstantsStatusUtils;
import com.cyber.online_books.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    /**
     * Lấy danh sách Comment theo truyện
     *
     * @param storyId    - id của truyện
     * @param pagenumber - số trang
     * @param type       - kiểu lấy danh sách
     * @return Page<CommentSummary>
     */
    @Override
    public Page<CommentSummary> getListCommentOfStory(Long storyId, int pagenumber, int type) {
        Page< CommentSummary > commentSummaryPage;
        if (type == 1) {
            Pageable pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_COMMENT_DEFAULT);
            commentSummaryPage = commentRepository
                    .findByStory_IdAndStatusOrderByCreateDateDesc(storyId, ConstantsStatusUtils.COMMENT_DISPLAY, pageable);
        } else {
            List< CommentSummary > commentSummaryList = commentRepository
                    .findByStory_IdAndStatusOrderByCreateDateDesc(storyId, ConstantsStatusUtils.COMMENT_DISPLAY);
            commentSummaryPage = new PageImpl<>(commentSummaryList);
        }
        return commentSummaryPage;
    }
    
    /**
     * Lưu Comment
     *
     * @param user        - Người đăng comment
     * @param story       - Comment của Truyện
     * @param commentText - Nội dung comment
     * @return true - nếu lưu comment thành công / false - nếu có lỗi xảy ra
     */
    @Override
    public boolean saveComment(User user, Story story, String commentText) {
        Comment newComment = new Comment();
        newComment.setStory(story);
        newComment.setUser(user);
        newComment.setContent(commentText);
        commentRepository.save(newComment);
        return newComment.getId() != null;
    }
}
