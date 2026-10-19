
package com.ShovanParia.Blogify.ServiceImp;

import com.ShovanParia.Blogify.Dto.CategoryDto;
import com.ShovanParia.Blogify.Entity.Category;
import com.ShovanParia.Blogify.ExceptionHandling.CategoryNotFoundException;
import com.ShovanParia.Blogify.Repository.CategotoryRepo;
import com.ShovanParia.Blogify.Service.CategorySer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategorySer {

    @Autowired
    private CategotoryRepo categotoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    // Create Category
    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        // Convert DTO into Entity
        Category category = modelMapper.map(
                categoryDto,
                Category.class
        );

        // Let MySQL generate the ID
        category.setId(null);

        // Save Entity in database
        Category savedCategory = categotoryRepo.save(category);

        // Convert saved Entity into DTO
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    // Update Category
    @Override
    public CategoryDto update(CategoryDto categoryDto, Long id) {

        Category existingCategory = categotoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + id
                ));

        // Update only fields provided by the client
        if (categoryDto.getTitle() != null) {
            existingCategory.setTitle(categoryDto.getTitle());
        }

        if (categoryDto.getDescription() != null) {
            existingCategory.setDescription(
                    categoryDto.getDescription()
            );
        }

        Category savedCategory = categotoryRepo.save(existingCategory);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    // Delete Category
    @Override
    public void deleteById(Long catId) {

        Category category = categotoryRepo.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + catId
                ));

        categotoryRepo.delete(category);
    }

    // Get All Categories
    @Override
    public List<CategoryDto> getAllCateGory() {

        return categotoryRepo.findAll()
                .stream()
                .map(category ->
                        modelMapper.map(category, CategoryDto.class)
                )
                .toList();
    }

    // Get Category By ID
    @Override
    public CategoryDto getById(Long id) {

        Category category = categotoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with ID: " + id
                ));

        return modelMapper.map(category, CategoryDto.class);
    }
}
