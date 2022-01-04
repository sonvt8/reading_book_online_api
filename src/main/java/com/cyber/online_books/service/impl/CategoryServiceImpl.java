package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Category;
import com.cyber.online_books.exception.domain.HttpMyException;
import com.cyber.online_books.exception.domain.NotFoundException;
import com.cyber.online_books.repository.CategoryRepository;
import com.cyber.online_books.response.CategorySummary;
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
    public List<CategorySummary> getListCategoryOfMenu(Integer status) {
        return categoryRepository.findAllByStatus(status);
    }

    /**
     * Tìm Category theo Id và status
     *
     * @param id
     * @param status
     * @return category - nếu tồn tại
     * @throws NotFoundException - nếu không tồn tại category có id và status
     */
    @Override
    public CategorySummary getCategoryByID(Integer id, Integer status) throws NotFoundException {
        return categoryRepository
                .findByIdAndStatus(id, status)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Page< Category > findCategoryBySearch(String search, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size, Sort.by("id").descending());
        if (search.trim().length() == 0) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findAllByNameContaining(search, pageable);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public boolean deleteCategory(Integer id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Category checkUnique(Integer id, String newCategoryName) throws HttpMyException {

        boolean isCreatingNew = (id == null || id == 0);

        Category newCategoryByName = categoryRepository.findCategoryByName(newCategoryName);
        if (newCategoryByName != null){
            if (isCreatingNew)
                throw new HttpMyException("thể loại này đã tồn tại");
            else
                if(newCategoryByName.getId() != id)
                    throw new HttpMyException("thể loại này đã tồn tại");
                return newCategoryByName;
        }

        return null;
    }

}
