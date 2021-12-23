package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.repository.CategoryRepository;
import com.cyber.online_books.response.CategoryResponse;
import com.cyber.online_books.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    /**
     * Lấy danh sách Thể loại của Menu
     *
     * @param status
     * @return List<Category> - danh sách thể loại
     */
    @Override
    public List< CategoryResponse > getListCategoryOfMenu(Integer status) {
        return categoryRepository.findAllByStatus(status);
    }

    @Override
    public Page< Category > findCategoryBySearch(String search, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size, Sort.by("id").descending());
        if (search.trim().length() == 0) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findAllByNameContaining(search, pageable);
    }

    @Override
    public boolean newCategory(Category category) {
        category = categoryRepository.save(category);
        return category.getId() != null;
    }
}
