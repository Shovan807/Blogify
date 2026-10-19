package com.ShovanParia.Blogify.Service;

import com.ShovanParia.Blogify.Dto.CategoryDto;

import java.util.List;

public interface CategorySer {
     CategoryDto create(CategoryDto categoryDto);

     CategoryDto update(CategoryDto categoryDto,Long id);

      void deleteById(Long catId );

     List<CategoryDto> getAllCateGory();

     CategoryDto getById(Long Id);
}
