package ru.practicum.enw.model.mapper.custom;

import org.springframework.stereotype.Component;
import ru.practicum.enw.model.category.CategoryDto;
import ru.practicum.enw.model.category.NewCategoryDto;
import ru.practicum.enw.model.entity.Category;

@Component
public class CategoryMapper {

    /**
     * Method for make CategoryEwm from DTO
     *
     * @param newCategoryDto CategoryEwmDto object
     * @return CategoryEwm object
     */
    public static Category fromNewToEntity(NewCategoryDto newCategoryDto) {

        return Category.builder().name(newCategoryDto.getName()).build();

    }

    /**
     * Method for transfer Category entity to CategoryDto
     *
     * @param category category object
     * @param category updated category
     * @return CategoryDto object
     */
    public static CategoryDto toDtoFromEntity(Category category) {

        return CategoryDto.builder().id(category.getId()).name(category.getName()).build();
    }

    /**
     * Method for transfer NewCategoryDto to Category entity
     *
     * @param id       identification number
     * @param category NewCategoryDto object
     * @return Category object
     */
    public static Category toDtoFromNewCategoryDtoWithId(long id, NewCategoryDto category) {

        return Category.builder().id(id).name(category.getName()).build();
    }

    public static Category fromDtoToEntity(CategoryDto categoryDto) {

        return Category.builder().id(categoryDto.getId()).name(categoryDto.getName()).build();
    }

}
