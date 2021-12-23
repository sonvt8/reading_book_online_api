package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Lấy danh sách Thể loại theo
     *
     * @param status
     * @return List<CategoryResponse> - danh sách thể loại
     */
    List<CategoryResponse> findAllByStatus(Integer status);

    Page< Category > findAllByNameContaining(String search, Pageable pageable);

}
