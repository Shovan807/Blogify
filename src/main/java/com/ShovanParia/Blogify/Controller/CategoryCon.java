package com.ShovanParia.Blogify.Controller;

import com.ShovanParia.Blogify.Dto.CategoryDto;
import com.ShovanParia.Blogify.Service.CategorySer;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryCon {

    @Autowired
    private CategorySer categorySer;

    // Create Category
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto createdCategory = categorySer.create(categoryDto);

        return new ResponseEntity<>(
                createdCategory,
                HttpStatus.CREATED
        );

    }

    // Update Category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Long id) {

        CategoryDto updatedCategory = categorySer.update(categoryDto, id);

        return ResponseEntity.ok(updatedCategory);
    }

    // Get Category By ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(
            @PathVariable Long id) {

        CategoryDto category = categorySer.getById(id);

        return ResponseEntity.ok(category);
    }

    // Get All Categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {

        List<CategoryDto> categories = categorySer.getAllCateGory();

        return ResponseEntity.ok(categories);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {

        categorySer.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}