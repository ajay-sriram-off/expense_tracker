package com.manager.task.services;

import com.manager.task.dtos.CategoryRequest;
import com.manager.task.dtos.CategoryResponse;
import com.manager.task.entities.Category;
import com.manager.task.exceptions.CategoryNotFoundException;
import com.manager.task.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category mapToCategory(CategoryRequest categoryRequest){
        Category cat = new Category();
        cat.setName(categoryRequest.getName());
        return cat;
    }

    // add Category
    public void addCategory(CategoryRequest categoryRequest) {
         categoryRepository.save(mapToCategory(categoryRequest));
    }

    // delete category
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }

    // update Category
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        categoryRepository.findById(id).map(existing -> {
            existing.setName(categoryRequest.getName());
            return categoryRepository.save(existing);
        }).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    // get All categories
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(category -> {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            return response;
        }).toList();
    }

    // get category by id
    public CategoryResponse getCategoryById(Long id) {
        CategoryResponse categoryResponse = new CategoryResponse();
       Category category= categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        categoryResponse.setName(category.getName());
        categoryResponse.setId(category.getId());
        return categoryResponse;
    }

}
