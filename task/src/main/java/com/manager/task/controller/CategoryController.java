package com.manager.task.controller;

import com.manager.task.dtos.CategoryRequest;
import com.manager.task.dtos.CategoryResponse;
import com.manager.task.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        categoryService.addCategory(categoryRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        CategoryResponse categoryResponse=categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategory(@PathVariable Long id){
         List<CategoryResponse> list = categoryService.getAllCategories();
         return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id ,@Valid @RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(id ,categoryRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
