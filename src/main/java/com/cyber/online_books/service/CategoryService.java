package com.cyber.online_books.service;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.exception.category.CategoryNotFoundException;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.response.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryService {
    /**
     * Lấy danh sách Thể loại của Menu
     *
     * @param status
     * @return List<CategorySummary> - danh sách thể loại
     */
    List<CategoryResponse> getListCategoryOfMenu(Integer status);

    /**
     * Tìm Category theo Id và status
     *
     * @param id
     * @param status
     * @return CategorySummary - nếu tồn tại
     * @throws Exception - nếu không tồn tại category có id và status
     */
    CategoryResponse getCategoryByID(Integer id, Integer status) throws Exception;

    /**
     * Tìm Category theo search
     *
     * @param search
     * @param pagenumber
     * @param size
     * @return
     */
    Page<Category> findCategoryBySearch(String search, Integer pagenumber, Integer size);

    /**
     *
     * @param id
     * @return
     */
    Category findCategoryById(Integer id);

    Category save(Category category);

    boolean deleteCategory(Integer id) throws CategoryNotFoundException;

    Category checkUnique(Integer id, String newCategoryName) throws HttpMyException;


}
