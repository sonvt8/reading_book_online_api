package com.cyber.online_books.repository;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.response.CategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Lấy danh sách Thể loại theo
     *
     * @param status
     * @return List<CategoryResponse> - danh sách thể loại
     */
    List<CategorySummary> findAllByStatus(Integer status);

    /**
     * Tìm category theo id và status
     *
     * @param id
     * @param status
     * @return Optional<CategorySummary>
     */
    Optional<CategorySummary> findByIdAndStatus(Integer id, Integer status);

    /**
     * Lấy danh sách Thể loại theo
     *
     * @param status
     * @param name
     * @return Category - Thể loại
     */
    Category findCategoryByNameAndStatus(String name, Integer status);

    Category findCategoryByName(String name);

    Page< Category > findAllByNameContaining(String search, Pageable pageable);


}
