package ru.practicum.enw.service.category;

import ru.practicum.enw.exceptions.NotFoundCustomException;
import ru.practicum.enw.model.category.CategoryDto;
import ru.practicum.enw.model.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    /**
     * Add new category
     *
     * @param category category for adding
     * @return CategoryEwm object
     */
    CategoryDto add(NewCategoryDto category);

    /**
     * Delete an existing catrgory
     *
     * @param id identification number of category
     * @throws NotFoundCustomException
     */
    void delete(long id) throws NotFoundCustomException;

    /**
     * Update an existing category
     *
     * @param id             identification number of category
     * @param newCategoryDto object with updating data
     * @return updated CategoryEwm object
     * @throws NotFoundCustomException
     */
    CategoryDto update(long id, NewCategoryDto newCategoryDto) throws NotFoundCustomException;

    /**
     * Get category by ID
     *
     * @param id identification number
     * @return CategoryDTO objecrt
     */
    CategoryDto getById(long id) throws NotFoundCustomException;

    /**
     * Get category list having parameters
     *
     * @param from the number of skipping records
     * @param sizw the number of list size
     * @return
     */
    List<CategoryDto> getCategoryListByParams(int from, int sizw);
}
